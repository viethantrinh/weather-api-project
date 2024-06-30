package net.branium.hourly;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.GeolocationException;
import net.branium.GeolocationService;
import net.branium.common.HourlyWeather;
import net.branium.common.Location;
import net.branium.location.LocationNotFoundException;
import net.branium.location.LocationService;
import net.branium.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/hourly")
@RequiredArgsConstructor
public class HourlyWeatherApiController {
    private final HourlyWeatherService hourlyWeatherService;
    private final GeolocationService geolocationService;
    private final HourlyWeatherMapper hourlyWeatherMapper;

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String clientIPAddress = Utilities.getIPAddress(request);
        try {
            int clientCurrentHour = Utilities.getCurrentHour(request);
            Location locationByIPAddress = geolocationService.getLocation(clientIPAddress);
            List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getHourlyWeatherByLocationAndCurrentHour(locationByIPAddress, clientCurrentHour);
            if (listHourlyWeather.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(toListHourWeatherDTO(listHourlyWeather));
        } catch (NumberFormatException | GeolocationException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable("locationCode") String locationCode, HttpServletRequest request) {
        try {
            int clientCurrentHour = Utilities.getCurrentHour(request);
            List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getHourlyWeatherByLocationCodeAndCurrentHour(locationCode, clientCurrentHour);
            if (listHourlyWeather.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(toListHourWeatherDTO(listHourlyWeather));
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        } catch (LocationNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    private HourlyWeatherListDTO toListHourWeatherDTO(List<HourlyWeather> hourlyWeatherList) {
        Location location = hourlyWeatherList.getFirst().getId().getLocation(); // get the location that these hourly weathers belong to
        return HourlyWeatherListDTO.builder()
                .location(location.getCityName() + ", " +
                        (location.getRegionName() == null ? "" : location.getRegionName() + ", ") +
                        location.getCountryName())
                .hourlyForecast(hourlyWeatherList.stream().map(hourlyWeatherMapper::toHourWeatherDTO).toList())
                .build();
    }

}
