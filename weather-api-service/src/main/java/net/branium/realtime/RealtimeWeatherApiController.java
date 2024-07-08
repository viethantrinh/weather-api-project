package net.branium.realtime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.daily.DailyWeatherApiController;
import net.branium.full.FullWeatherApiController;
import net.branium.hourly.HourlyWeatherApiController;
import net.branium.location.GeolocationService;
import net.branium.util.Utilities;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping("/v1/realtime")
@RequiredArgsConstructor
public class RealtimeWeatherApiController {
    private final RealtimeWeatherService realtimeWeatherService;
    private final GeolocationService geolocationService;
    private final RealtimeWeatherMapper realtimeWeatherMapper;

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String clientIPAddress = Utilities.getIPAddress(request);
        Location locationFromIP = geolocationService.getLocation(clientIPAddress);
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocationCountryCodeAndCityName(locationFromIP);
        RealtimeWeatherDTO realtimeWeatherDTO = realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather);
        return ResponseEntity.ok(addLinkByIP(realtimeWeatherDTO));
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocationCode(locationCode);
        RealtimeWeatherDTO realtimeWeatherDTO = realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather);
        return ResponseEntity.ok(addLinkByLocationCode(realtimeWeatherDTO, locationCode));
    }

    @PutMapping(path = "/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode,
                                                                 @RequestBody @Valid RealtimeWeatherDTO realtimeWeatherRequest) {
        RealtimeWeather realtimeWeather = realtimeWeatherMapper.toRealtimeWeather(realtimeWeatherRequest);
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherService
                .updateRealtimeWeather(locationCode, realtimeWeather);
        RealtimeWeatherDTO dto = realtimeWeatherMapper.toRealtimeWeatherDTO(updatedRealtimeWeather);
        return ResponseEntity.ok(addLinkByLocationCode(dto, locationCode));

    }

    private RealtimeWeatherDTO addLinkByIP(RealtimeWeatherDTO dto) {
        dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast"));
        return dto;
    }

    private RealtimeWeatherDTO addLinkByLocationCode(RealtimeWeatherDTO dto, String locationCode) {
        dto.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode)).withSelfRel());
        dto.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null)).withRel("hourly_forecast"));
        dto.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode)).withRel("daily_forecast"));
        dto.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_forecast"));
        return dto;
    }
}
