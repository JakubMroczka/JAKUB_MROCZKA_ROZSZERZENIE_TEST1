package pl.kurs.fx.interfaces;

public interface RateProvider {
    double getRate(String from, String to) throws Exception;
}