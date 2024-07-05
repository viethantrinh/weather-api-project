package net.branium.daily;

import lombok.RequiredArgsConstructor;
import net.branium.common.DailyWeather;
import net.branium.common.Location;
import net.branium.exception.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyWeatherService {

    private final DailyWeatherRepository dailyWeatherRepo;
    private final LocationRepository locationRepo;

    public List<DailyWeather> getDailyWeatherByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationFromDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException(countryCode, cityName));
        return dailyWeatherRepo.findByLocationCode(locationFromDB.getCode());
    }

    public List<DailyWeather> getDailyWeatherByLocationCode(String locationCode) {
        Location locationFromDB = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException(locationCode));
        return dailyWeatherRepo.findByLocationCode(locationFromDB.getCode());
    }
}
