package net.branium.exception;


public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String code) {
        super("Can not find location with code: " + code);
    }
}
