package net.branium.hourly;

import lombok.RequiredArgsConstructor;
import net.branium.common.HourlyWeather;
import net.branium.common.Location;
import net.branium.location.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HourlyWeatherService {
    private final HourlyWeatherRepository hourlyWeatherRepo;
    private final LocationRepository locationRepo;

    public List<HourlyWeather> getHourlyWeatherByLocationAndCurrentHour(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationFromDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given country code and city name"));
        return hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationFromDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getHourlyWeatherByLocationCodeAndCurrentHour(String locationCode, int currentHour) throws LocationNotFoundException {
        Location locationFromDB = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given location code"));
        return hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationFromDB.getCode(), currentHour);
    }
}
