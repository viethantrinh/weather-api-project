package net.branium.daily;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.DailyWeather;
import net.branium.common.Location;
import net.branium.full.FullWeatherApiController;
import net.branium.hourly.HourlyWeatherApiController;
import net.branium.hourly.HourlyWeatherDTO;
import net.branium.location.GeolocationService;
import net.branium.realtime.RealtimeWeatherApiController;
import net.branium.util.Utilities;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<?> listDailyForecastByIPAddress(HttpServletRequest request) {
        String clientIPAddress = Utilities.getIPAddress(request);
        Location location = geolocationService.getLocation(clientIPAddress);
        List<DailyWeather> dailyWeathersByLocation = dailyWeatherService.getDailyWeatherByLocation(location);
        return dailyWeathersByLocation.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(addLinkByIPAddress(toDailyWeatherListDTO(dailyWeathersByLocation)));
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> listDailyForecastByLocationCode(@PathVariable("locationCode") String locationCode) {
        List<DailyWeather> dailyWeathersByLocation = dailyWeatherService.getDailyWeatherByLocationCode(locationCode);
        return dailyWeathersByLocation.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(addLinkByLocationCode(toDailyWeatherListDTO(dailyWeathersByLocation), locationCode));
    }

    @PutMapping(path = "/{locationCode}")
    @Validated
    public ResponseEntity<?> updateDailyForecast(@PathVariable("locationCode") String locationCode,
                                                 @RequestBody @Valid List<DailyWeatherDTO> dailyWeatherDTOListRequest) {
        if (dailyWeatherDTOListRequest.isEmpty()) return ResponseEntity.badRequest().build();
        List<DailyWeather> dailyWeatherListUpdated = dailyWeatherService
                .updateDailyWeatherByLocationCode(locationCode, toDailyWeatherList(dailyWeatherDTOListRequest));
        return ResponseEntity.ok(
                addLinkByLocationCode(toDailyWeatherListDTO(dailyWeatherListUpdated), locationCode)
        );
    }

    private EntityModel<DailyWeatherListDTO> addLinkByIPAddress(DailyWeatherListDTO dailyWeatherListDTO) {
        EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dailyWeatherListDTO);
        entityModel.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).withSelfRel());
        entityModel.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).withRel("realtime"));
        entityModel.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).withRel("hourly_forecast"));
        entityModel.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).withRel("full_weather"));
        return entityModel;
    }

    private EntityModel<DailyWeatherListDTO> addLinkByLocationCode(DailyWeatherListDTO dailyWeatherListDTO, String locationCode) {
        EntityModel<DailyWeatherListDTO> entityModel = EntityModel.of(dailyWeatherListDTO);
        entityModel.add(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode)).withSelfRel());
        entityModel.add(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode)).withRel("realtime"));
        entityModel.add(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null)).withRel("hourly_forecast"));
        entityModel.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_weather"));
        return entityModel;
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

    private List<DailyWeather> toDailyWeatherList(List<DailyWeatherDTO> dailyWeatherDTOList) {
        return dailyWeatherDTOList
                .stream()
                .map(dailyWeatherMapper::toDailyWeather)
                .toList();
    }
}
