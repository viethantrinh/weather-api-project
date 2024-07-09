package net.branium.full;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.branium.common.DailyWeather;
import net.branium.common.HourlyWeather;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.daily.DailyWeatherMapper;
import net.branium.exception.BadRequestException;
import net.branium.hourly.HourlyWeatherMapper;
import net.branium.location.GeolocationService;
import net.branium.realtime.RealtimeWeatherMapper;
import net.branium.util.Utilities;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/full")
@RequiredArgsConstructor
public class FullWeatherApiController {
    private final FullWeatherService fullWeatherService;
    private final GeolocationService geolocationService;
    private final RealtimeWeatherMapper realtimeWeatherMapper;
    private final HourlyWeatherMapper hourlyWeatherMapper;
    private final DailyWeatherMapper dailyWeatherMapper;
    private final FullWeatherModelAssembler fullWeatherModelAssembler;

    @GetMapping
    public ResponseEntity<?> getFullWeatherByIPAddress(HttpServletRequest request) {
        String clientIPAddress = Utilities.getIPAddress(request);
        Location location = geolocationService.getLocation(clientIPAddress);
        Location fullWeatherByLocation = fullWeatherService.getFullWeatherByLocation(location);
        FullWeatherDTO fullWeatherDTO = toFullWeatherDTO(fullWeatherByLocation);
        return ResponseEntity.ok(fullWeatherModelAssembler.toModel(fullWeatherDTO));
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        Location fullWeatherByLocation = fullWeatherService.getFullWeatherByLocationCode(locationCode);
        FullWeatherDTO fullWeatherDTO = toFullWeatherDTO(fullWeatherByLocation);
        return ResponseEntity.ok(
                addLinkByLocationCode(fullWeatherDTO, locationCode)
        );
    }

    @PutMapping(path = "/{locationCode}")
    public ResponseEntity<?> updateFullWeather(@PathVariable("locationCode") String locationCode,
                                               @RequestBody @Valid FullWeatherDTO fullWeatherDTO) {
        if (fullWeatherDTO.getHourlyWeather().isEmpty()) {
            throw new BadRequestException("Bad request");
        }

        if (fullWeatherDTO.getDailyWeather().isEmpty()) {
            throw new BadRequestException("Bad request");
        }

        Location locationInRequest = fullWeatherDTOToLocation(fullWeatherDTO);
        Location locationUpdated = fullWeatherService.updateFullWeatherByLocationCode(locationCode, locationInRequest);
        return ResponseEntity.ok(
                addLinkByLocationCode(toFullWeatherDTO(locationUpdated), locationCode)
        );
    }

    private EntityModel<FullWeatherDTO> addLinkByLocationCode(FullWeatherDTO fullWeatherDTO, String locationCode) {
        EntityModel<FullWeatherDTO> entityModel = EntityModel.of(fullWeatherDTO);
        entityModel.add(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode)).withSelfRel());
        return entityModel;
    }


    private Location fullWeatherDTOToLocation(FullWeatherDTO fullWeatherDTO) {
        var realtimeWeatherDTO = fullWeatherDTO.getRealtimeWeather();
        var hourlyWeatherDTOList = fullWeatherDTO.getHourlyWeather();
        var dailyWeatherDTOList = fullWeatherDTO.getDailyWeather();
        return Location
                .builder()
                .realtimeWeather(realtimeWeatherMapper.toRealtimeWeather(realtimeWeatherDTO))
                .hourlyWeathers(hourlyWeatherDTOList.stream().map(hourlyWeatherMapper::toHourWeather).toList())
                .dailyWeathers(dailyWeatherDTOList.stream().map(dailyWeatherMapper::toDailyWeather).toList())
                .build();
    }

    private FullWeatherDTO toFullWeatherDTO(Location location) {
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();
        List<HourlyWeather> hourlyWeathers = location.getHourlyWeathers();
        List<DailyWeather> dailyWeathers = location.getDailyWeathers();
        return FullWeatherDTO
                .builder()
                .location(location.getCityName() + ", " +
                        (location.getRegionName() == null ? "" : location.getRegionName() + ", ") +
                        location.getCountryName())
                .realtimeWeather(realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather))
                .hourlyWeather(hourlyWeathers.stream().map(hourlyWeatherMapper::toHourWeatherDTO).toList())
                .dailyWeather(dailyWeathers.stream().map(dailyWeatherMapper::toDailyWeatherDTO).toList())
                .build();
    }
}
