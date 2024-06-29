package net.branium.realtime;

import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RealtimeWeatherService {
    private final RealtimeWeatherRepository realtimeWeatherRepo;
    private final LocationRepository locationRepo;
    private final RealtimeWeatherMapper realtimeWeatherMapper;

    public RealtimeWeather getRealtimeWeatherByLocationCountryCodeAndCityName(Location location)
            throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        return realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given country code and city name"));
    }

    public RealtimeWeather getRealtimeWeatherByLocationCode(String locationCode)
            throws LocationNotFoundException {
        return realtimeWeatherRepo.findByLocationCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given location code"));
    }

    /**
     * Update full realtime weather, not partial update
     * @param locationCode locationCode
     * @param realtimeWeatherRequest
     * @return realtime weather object that has been updated
     * @throws LocationNotFoundException if no location found
     */
    public RealtimeWeather updateRealtimeWeather(String locationCode, RealtimeWeather realtimeWeatherRequest)
            throws LocationNotFoundException {
        Location location = locationRepo.findByCode(locationCode)
                .orElseThrow(() -> new LocationNotFoundException("No location found with the given code"));

        realtimeWeatherRequest.setLocation(location);
        realtimeWeatherRequest.setLastUpdated(LocalDateTime.now());

        if (location.getRealtimeWeather() == null) { // in case the location don't have the managed realtime weather
            location.setRealtimeWeather(realtimeWeatherRequest);
            Location savedLocation = locationRepo.save(location);
            return savedLocation.getRealtimeWeather();
        }

        return realtimeWeatherRepo.save(realtimeWeatherRequest);
    }
}
