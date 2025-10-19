package pl.kurs.fx;

import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.service.CurrencyService;

import java.time.Clock;

public class Main {
    public static void main(String[] args) {
        RateProvider provider = FxInitializer.rateProvider();
        TtlRateCache cache = FxInitializer.rateCache(10_000L, Clock.systemUTC());
        CurrencyService service = FxInitializer.currencyService(provider, cache);

        System.out.println("provider mocked in tests");
    }
}