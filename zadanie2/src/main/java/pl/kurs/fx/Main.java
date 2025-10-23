package pl.kurs.fx;

import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.service.CurrencyService;

import java.time.Clock;

public class Main {
    public static void main(String[] args) {
        RateProvider provider = FxInitializer.rateProvider();
        long ttlMillis = 10_000L;

        CurrencyService service = FxInitializer.currencyService(provider, ttlMillis, Clock.systemUTC());

        System.out.println("provider mocked in tests");
    }
}