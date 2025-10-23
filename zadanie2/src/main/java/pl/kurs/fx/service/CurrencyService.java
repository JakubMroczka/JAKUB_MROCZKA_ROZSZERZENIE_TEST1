package pl.kurs.fx.service;


import pl.kurs.fx.exceptions.ExchangeRateFetchException;
import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.model.CurrencyPair;

import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CurrencyService {
    private final RateProvider provider;
    private final long ttlMillis;
    private final Clock clock;

    private static final class Entry {
        final double value;
        final long expiresAtMillis;

        Entry(double value, long expiresAtMillis) {
            this.value = value;
            this.expiresAtMillis = expiresAtMillis;
        }
    }

    private final ConcurrentMap<CurrencyPair, Entry> cache = new ConcurrentHashMap<>();

    public CurrencyService(RateProvider provider, long ttlMillis, Clock clock) {
        if (ttlMillis <= 0) throw new IllegalArgumentException("ttlMillis <= 0");
        this.provider = Objects.requireNonNull(provider, "provider");
        this.ttlMillis = ttlMillis;
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public double exchange(String currencyFrom, String currencyTo, double amount) {
        if (amount < 0) throw new IllegalArgumentException("amount < 0");
        if (amount == 0.0) return 0.0;
        if (currencyFrom.equalsIgnoreCase(currencyTo)) return amount;

        CurrencyPair pair = new CurrencyPair(currencyFrom, currencyTo);

        Entry e = cache.compute(pair, (k, old) -> {
            long now = clock.millis();
            if (old == null || now >= old.expiresAtMillis) {
                double rate;
                try {
                    rate = provider.getRate(k.from(), k.to());
                } catch (Exception ex) {
                    throw new ExchangeRateFetchException(
                            "failed to fetch rate " + k.from() + "-" + k.to(), ex);
                }
                return new Entry(rate, now + ttlMillis);
            }
            return old;
        });

        return amount * e.value;
    }
}