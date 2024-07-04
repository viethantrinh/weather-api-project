package net.branium.hourly;

import lombok.RequiredArgsConstructor;
import net.branium.common.HourlyWeather;
import net.branium.common.Location;
import net.branium.exception.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HourlyWeatherService {
    private final HourlyWeatherRepository hourlyWeatherRepo;
    private final LocationRepository locationRepo;

    public List<HourlyWeather> getHourlyWeatherByLocationAndCurrentHour(Location location, int currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationFromDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given country code and city name"));
        return hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationFromDB.getCode(), currentHour);
    }

    public List<HourlyWeather> getHourlyWeatherByLocationCodeAndCurrentHour(String locationCode, int currentHour) {
        Location locationFromDB = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given location code"));
        return hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationFromDB.getCode(), currentHour);
    }

    public List<HourlyWeather> updateHourlyWeatherByLocationCode(String locationCode,
                                                                 List<HourlyWeather> hourlyWeatherListRequest) throws LocationNotFoundException {
        Location locationFromDB = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given location code"));
        for (HourlyWeather item : hourlyWeatherListRequest) { // set location to the hourly weather list request
            item.getId().setLocation(locationFromDB);
        }
        List<HourlyWeather> hourlyWeatherListFromDB = locationFromDB.getHourlyWeathers(); // take all the locations already in the database
        hourlyWeatherListFromDB.removeIf(item -> !hourlyWeatherListRequest.contains(item)); // remove the item if it is not existed in the list request
        return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherListRequest);
    }
}
