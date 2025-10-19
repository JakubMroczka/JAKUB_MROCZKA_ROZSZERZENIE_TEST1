package pl.kurs.fx.service;


import pl.kurs.fx.TtlRateCache;
import pl.kurs.fx.exceptions.ExchangeRateFetchException;
import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.model.CurrencyPair;

public class CurrencyService {
    private final RateProvider provider;
    private final TtlRateCache cache;

    public CurrencyService(RateProvider provider, TtlRateCache cache) {
        this.provider = provider;
        this.cache = cache;
    }

    public double exchange(String currencyFrom, String currencyTo, double amount) {
        if (amount < 0) throw new IllegalArgumentException("amount < 0");
        if (amount == 0) return 0.0;
        if (currencyFrom.equalsIgnoreCase(currencyTo)) return amount;

        CurrencyPair pair = new CurrencyPair(currencyFrom, currencyTo);

        double rate = cache.get(pair, () -> {
            try {
                return provider.getRate(pair.from(), pair.to());
            } catch (Exception e) {
                throw new ExchangeRateFetchException(
                        "Failed to fetch rate " + pair.from() + "->" + pair.to(), e);
            }
        });

        return amount * rate;
    }
}