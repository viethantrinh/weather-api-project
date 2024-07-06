package net.branium.ip2locaton;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import net.branium.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class IP2LocationTests {
    IP2Location locator;
    byte[] data;

    @BeforeEach
    void setUp() throws IOException {
        locator = new IP2Location();
        data = null;
        InputStream inputStream = getClass().getResourceAsStream(Constants.IP2DBPATH);
        if (inputStream != null) {
            data = inputStream.readAllBytes();
            locator.Open(data);
            inputStream.close();
        }
    }

    @Test
    void testInvalidIP() throws IOException {
        String ipAddress = "abc";
        IPResult ipResult = locator.IPQuery(ipAddress);
        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
        System.out.println(ipResult);
    }

    @Test
    void testValidIP1() throws IOException {
        String ipAddress = "108.30.178.78";
        IPResult ipResult = locator.IPQuery(ipAddress);
        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("New York City");
        System.out.println(ipResult);
    }

    @Test
    void testValidIP2() throws IOException {
        String ipAddress = "103.48.198.141";
        IPResult ipResult = locator.IPQuery(ipAddress);
        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("Delhi");
        assertThat(ipResult.getCountryShort()).isEqualTo("IN");
    }
}
