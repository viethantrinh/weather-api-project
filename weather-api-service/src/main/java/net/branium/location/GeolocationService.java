package net.branium.location;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.Location;
import net.branium.exception.GeolocationException;
import net.branium.util.Constants;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static net.branium.util.Constants.IP2DBPATH;

@Slf4j
@Service
public final class GeolocationService {
    private final IP2Location ip2locator = new IP2Location();

    public GeolocationService() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(IP2DBPATH);
            byte[] data = inputStream.readAllBytes();
            ip2locator.Open(data);
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) {
        try {
            IPResult ipResult = ip2locator.IPQuery(ipAddress);
            if (!"OK".equals(ipResult.getStatus())) {
                throw new GeolocationException("Geolocation failed with status: " + ipResult.getStatus());
            }
            return Location.builder()
                    .cityName(ipResult.getCity())
                    .regionName(ipResult.getRegion())
                    .countryCode(ipResult.getCountryShort())
                    .countryName(ipResult.getCountryLong())
                    .build();
        } catch (IOException ex) {
            throw new GeolocationException("Error querying IP2Location database", ex);
        }
    }
}
