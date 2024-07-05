package net.branium.exception;


public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String code) {
        super("Can not find location with code: " + code);
    }

    public LocationNotFoundException(String countryCode, String cityName) {
        super("Can not find location with the given countryCode: " + countryCode + " and cityName: " + cityName);
    }
}
