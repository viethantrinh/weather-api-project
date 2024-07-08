package net.branium.hourly;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.HourlyWeather;
import net.branium.common.Location;
import net.branium.daily.DailyWeatherApiController;
import net.branium.exception.BadRequestException;
import net.branium.full.FullWeatherApiController;
import net.branium.location.GeolocationService;
import net.branium.realtime.RealtimeWeatherApiController;
import net.branium.util.Utilities;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
            return ResponseEntity.ok(
                    addLinkByIPAddress(toListHourWeatherDTO(listHourlyWeather))
            );
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
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
            return ResponseEntity.ok(
                    addLinkByLocationCode(toListHourWeatherDTO(listHourlyWeather), locationCode)
            );
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/{locationCode}")
    @Validated
    public ResponseEntity<?> updateHourlyWeatherByLocationCode(@PathVariable("locationCode") String locationCode,
                                                               @RequestBody @Valid List<HourlyWeatherDTO> hourlyWeatherDTOListRequest) {
        if (hourlyWeatherDTOListRequest == null || hourlyWeatherDTOListRequest.isEmpty()) {
            throw new BadRequestException("Hourly forecast is empty");
        }

        List<HourlyWeather> hourlyWeatherList = toListHourWeather(hourlyWeatherDTOListRequest);

        List<HourlyWeather> updatedHourlyWeatherList = hourlyWeatherService
                .updateHourlyWeatherByLocationCode(locationCode, hourlyWeatherList);
        HourlyWeatherListDTO hourlyWeatherListDTO = toListHourWeatherDTO(updatedHourlyWeatherList);
        return ResponseEntity.ok(
                addLinkByLocationCode(hourlyWeatherListDTO, locationCode)
        );

    }

    private HourlyWeatherListDTO addLinkByIPAddress(HourlyWeatherListDTO hourlyWeatherListDTO) {
        hourlyWeatherListDTO.add(
                linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).withSelfRel()
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).withRel("realtime")
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).withRel("daily_forecast")
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).withRel("full_forecast")
        );
        return hourlyWeatherListDTO;
    }

    private HourlyWeatherListDTO addLinkByLocationCode(HourlyWeatherListDTO hourlyWeatherListDTO, String locationCode) {
        hourlyWeatherListDTO.add(
                linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(locationCode, null)).withSelfRel()
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(locationCode)).withRel("realtime")
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode)).withRel("daily_forecast")
        );
        hourlyWeatherListDTO.add(
                linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode)).withRel("full_forecast")
        );
        return hourlyWeatherListDTO;
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

    private List<HourlyWeather> toListHourWeather(List<HourlyWeatherDTO> hourlyWeatherDTOList) {
        return hourlyWeatherDTOList
                .stream()
                .map(hourlyWeatherMapper::toHourWeather)
                .collect(Collectors.toList());
    }

}
