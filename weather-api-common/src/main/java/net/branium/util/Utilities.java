package net.branium.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
    private static DateTimeFormatter iso8601Formatter = DateTimeFormatter.ISO_INSTANT;

    public static String toISO8601(LocalDateTime time) {
        return time.format(iso8601Formatter);
    }

}
