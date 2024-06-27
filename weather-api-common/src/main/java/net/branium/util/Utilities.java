package net.branium.util;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
    private static DateTimeFormatter iso8601Formatter = DateTimeFormatter.ISO_INSTANT;

    public static String toISO8601(LocalDateTime time) {
        return time.format(iso8601Formatter);
    }

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        return ip == null || ip.isEmpty() ? request.getRemoteAddr() : ip;
    }


}
