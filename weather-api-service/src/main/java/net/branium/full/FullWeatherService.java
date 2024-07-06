package net.branium.full;

import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import net.branium.exception.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FullWeatherService {
    private final LocationRepository locationRepo;

    public Location getFullWeatherByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        return locationRepo.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
    }

    public Location getFullWeatherByLocationCode(String locationCode) {
        return locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException(locationCode));
    }

    public Location updateFullWeatherByLocationCode(String locationCode, Location locationInRequest) {
        Location locationFromDB = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException(locationCode));

        locationInRequest.setCode(locationFromDB.getCode());
        locationInRequest.setRegionName(locationFromDB.getRegionName());
        locationInRequest.setCountryCode(locationFromDB.getCountryCode());
        locationInRequest.setCountryName(locationFromDB.getCountryName());
        locationInRequest.setCityName(locationFromDB.getCityName());
        locationInRequest.setEnabled(locationFromDB.isEnabled());

        locationInRequest.getRealtimeWeather().setLocation(locationFromDB);
        locationInRequest.getRealtimeWeather().setLastUpdated(LocalDateTime.now());

        locationInRequest.getHourlyWeathers()
                .forEach(hourlyWeather -> hourlyWeather.getId().setLocation(locationFromDB));
        locationFromDB.getHourlyWeathers()
                .removeIf(hourlyWeather -> !locationInRequest.getHourlyWeathers().contains(hourlyWeather));

        locationInRequest.getDailyWeathers()
                .forEach(dailyWeather -> dailyWeather.getId().setLocation(locationFromDB));
        locationFromDB.getDailyWeathers()
                .removeIf(dailyWeather -> !locationInRequest.getDailyWeathers().contains(dailyWeather));

        return locationRepo.save(locationInRequest);
    }
}
