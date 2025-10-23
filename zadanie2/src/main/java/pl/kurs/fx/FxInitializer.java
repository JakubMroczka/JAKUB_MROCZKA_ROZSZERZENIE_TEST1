package pl.kurs.fx;

import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.service.CurrencyService;

import java.time.Clock;

public class FxInitializer {
    private FxInitializer() {
    }

    public static RateProvider rateProvider() {
        return (from, to) -> {
            throw new UnsupportedOperationException("HTTP not required here");
        };
    }

    public static CurrencyService currencyService(RateProvider provider, long ttlMillis, Clock clock) {
        return new CurrencyService(provider, ttlMillis, clock);
    }
}
