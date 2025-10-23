package pl.kurs.fx;

import org.junit.Test;
import pl.kurs.fx.exceptions.ExchangeRateFetchException;
import pl.kurs.fx.interfaces.RateProvider;
import pl.kurs.fx.service.CurrencyService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    private static class MutableClock extends Clock {
        private long millis;
        private final ZoneId zone = ZoneId.systemDefault();

        MutableClock(long millis) {
            this.millis = millis;
        }

        void plusMillis(long add) {
            this.millis += add;
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public long millis() {
            return millis;
        }

        @Override
        public Instant instant() {
            return Instant.ofEpochMilli(millis);
        }
    }

    @Test
    public void shouldUseCacheWithinTtl() throws Exception {
        RateProvider provider = mock(RateProvider.class);
        MutableClock clock = new MutableClock(1_000_000L);
        CurrencyService service = new CurrencyService(provider, 10_000, clock);

        when(provider.getRate("USD", "PLN")).thenReturn(4.00);

        double a1 = service.exchange("USD", "PLN", 10);
        double a2 = service.exchange("USD", "PLN", 5);

        assertEquals(40.0, a1, 1e-9);
        assertEquals(20.0, a2, 1e-9);
        verify(provider, times(1)).getRate("USD", "PLN");
    }

    @Test
    public void shouldRefreshAfterTtlExpires() throws Exception {
        RateProvider provider = mock(RateProvider.class);
        when(provider.getRate("USD", "PLN")).thenReturn(4.0, 4.1);

        MutableClock clock = new MutableClock(System.currentTimeMillis());
        CurrencyService service = new CurrencyService(provider, 10_000, clock);

        assertEquals(40.0, service.exchange("USD", "PLN", 10), 1e-9);

        clock.plusMillis(10_001);
        assertEquals(41.0, service.exchange("USD", "PLN", 10), 1e-9);

        verify(provider, times(2)).getRate("USD", "PLN");
    }

    @Test
    public void shouldBeThreadSafe_underParallelCalls() throws Exception {
        RateProvider provider = mock(RateProvider.class);
        when(provider.getRate("GBP", "PLN")).thenReturn(5.0);

        MutableClock clock = new MutableClock(System.currentTimeMillis());
        CurrencyService service = new CurrencyService(provider, 60_000, clock);

        int threads = 20;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Double>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> service.exchange("GBP", "PLN", 2.0)));
        }
        for (Future<Double> f : futures) {
            assertEquals(10.0, f.get(), 1e-9);
        }
        pool.shutdown();

        verify(provider, times(1)).getRate("GBP", "PLN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeAmount() {
        RateProvider provider = mock(RateProvider.class);
        CurrencyService service = new CurrencyService(provider, 10_000, Clock.systemUTC());
        service.exchange("USD", "PLN", -1);
    }

    @Test
    public void shouldReturnZeroAndNotCallProvider_whenAmountIsZero() {
        RateProvider provider = mock(RateProvider.class);
        CurrencyService service = new CurrencyService(provider, 10_000, Clock.systemUTC());

        double result = service.exchange("USD", "PLN", 0.0);

        assertEquals(0.0, result, 1e-9);
        verifyNoInteractions(provider);
    }

    @Test
    public void shouldReturnAmountWhenCurrenciesEqual() {
        RateProvider provider = mock(RateProvider.class);
        CurrencyService service = new CurrencyService(provider, 10_000, Clock.systemUTC());

        double result = service.exchange("usd", "USD", 123.45);

        assertEquals(123.45, result, 1e-9);
        verifyNoInteractions(provider);
    }

    @Test
    public void shouldNormalizeCodesToUppercase() throws Exception {
        RateProvider p = mock(RateProvider.class);
        when(p.getRate("USD", "PLN")).thenReturn(4.0);

        CurrencyService s = new CurrencyService(p, 10_000, Clock.systemUTC());
        assertEquals(8.0, s.exchange("usd", "pln", 2.0), 1e-9);

        verify(p, times(1)).getRate("USD", "PLN");
    }

    @Test(expected = ExchangeRateFetchException.class)
    public void shouldPropagateAsCustomExceptionWhenProviderFails() throws Exception {
        RateProvider p = mock(RateProvider.class);
        when(p.getRate("USD", "PLN")).thenThrow(new Exception("booo"));

        CurrencyService s = new CurrencyService(p, 10_000, Clock.systemUTC());
        s.exchange("USD", "PLN", 1);
    }
}