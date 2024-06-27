package net.branium;

public class GeolocationServiceException extends RuntimeException {

    public GeolocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeolocationServiceException(String message) {
        super(message);
    }
}
