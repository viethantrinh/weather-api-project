package net.branium.realtime;

import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.LocationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepo;

    public RealtimeWeather getRealtimeWeatherByLocationCountryCodeAndCityName(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();

        Optional<RealtimeWeather> realtimeWeatherOpt = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName);

        if (realtimeWeatherOpt.isEmpty()) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }

        return realtimeWeatherOpt.get();
    }

    public RealtimeWeather getRealtimeWeatherByLocationCode(String locationCode) throws LocationNotFoundException {
        Optional<RealtimeWeather> realtimeWeatherOpt = realtimeWeatherRepo.findByLocationCode(locationCode);

        if (realtimeWeatherOpt.isEmpty()) {
            throw new LocationNotFoundException("No location found with the given location code");
        }

        return realtimeWeatherOpt.get();
    }
}
