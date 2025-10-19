package pl.kurs.fx.interfaces;

@FunctionalInterface
public interface RateProvider {
    double getRate(String from, String to) throws Exception;
}