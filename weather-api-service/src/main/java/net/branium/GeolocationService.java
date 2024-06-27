package net.branium;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.Location;
import net.branium.util.Constants;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public final class GeolocationService {
    private final IP2Location ip2locator = new IP2Location();

    public GeolocationService() {
        try {
            ip2locator.Open(Constants.IP2DBPATH);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) {
        try {
            IPResult ipResult = ip2locator.IPQuery(ipAddress);
            if (!"OK".equals(ipResult.getStatus())) {
                throw new GeolocationServiceException("Geolocation faild with status: " + ipResult.getStatus());
            }
            return Location.builder()
                    .cityName(ipResult.getCity())
                    .regionName(ipResult.getRegion())
                    .countryCode(ipResult.getCountryShort())
                    .countryName(ipResult.getCountryLong())
                    .build();
        } catch (IOException ex) {
            throw new GeolocationServiceException("IP2Location file is not exists!", ex);
        }
    }
}
