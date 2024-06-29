package net.branium.ip2locaton;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class IP2LocationTests {
    private final String IP2DBPath = "../ ip2locationdb/IP2LOCATION-LITE-DB3.BIN";

    @Test
    void testInvalidIP() throws IOException {
        IP2Location locator = new IP2Location();
        locator.Open(IP2DBPath);

        String ipAddress = "abc";

        IPResult ipResult = locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("INVALID_IP_ADDRESS");
        System.out.println(ipResult);
    }

    @Test
    void testValidIP1() throws IOException {
        IP2Location locator = new IP2Location();
        locator.Open(IP2DBPath);

        String ipAddress = "108.30.178.78";

        IPResult ipResult = locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("New York City");
        System.out.println(ipResult);
    }

    @Test
    void testValidIP2() throws IOException {
        IP2Location locator = new IP2Location();
        locator.Open(IP2DBPath);

        String ipAddress = "103.48.198.141";

        IPResult ipResult = locator.IPQuery(ipAddress);

        assertThat(ipResult.getStatus()).isEqualTo("OK");
        assertThat(ipResult.getCity()).isEqualTo("Delhi");
        assertThat(ipResult.getCountryShort()).isEqualTo("IN");
    }
}
