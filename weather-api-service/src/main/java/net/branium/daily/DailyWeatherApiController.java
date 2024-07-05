package net.branium.daily;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.DailyWeather;
import net.branium.common.Location;
import net.branium.hourly.HourlyWeatherDTO;
import net.branium.location.GeolocationService;
import net.branium.util.Utilities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/daily")
@RequiredArgsConstructor
public class DailyWeatherApiController {
    private final DailyWeatherService dailyWeatherService;
    private final GeolocationService geolocationService;
    private final DailyWeatherMapper dailyWeatherMapper;

    @GetMapping
    public ResponseEntity<DailyWeatherListDTO> listDailyForecastByIPAddress(HttpServletRequest request) {
        String clientIPAddress = Utilities.getIPAddress(request);
        Location location = geolocationService.getLocation(clientIPAddress);
        List<DailyWeather> dailyWeathersByLocation = dailyWeatherService.getDailyWeatherByLocation(location);
        return dailyWeathersByLocation.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(toDailyWeatherListDTO(dailyWeathersByLocation));
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<DailyWeatherListDTO> listDailyForecastByLocationCode(@PathVariable("locationCode") String locationCode) {
        List<DailyWeather> dailyWeathersByLocation = dailyWeatherService.getDailyWeatherByLocationCode(locationCode);
        return dailyWeathersByLocation.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(toDailyWeatherListDTO(dailyWeathersByLocation));
    }

    private DailyWeatherListDTO toDailyWeatherListDTO(List<DailyWeather> dailyWeatherList) {
        Location location = dailyWeatherList.getFirst().getId().getLocation();

        return DailyWeatherListDTO
                .builder()
                .location(location.getCityName() + ", " +
                        (location.getRegionName() == null ? "" : location.getRegionName() + ", ") +
                        location.getCountryName())
                .dailyWeatherDTOList(dailyWeatherList.stream().map(dailyWeatherMapper::toDailyWeatherDTO).toList())
                .build();
    }
}
