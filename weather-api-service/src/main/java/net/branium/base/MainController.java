package net.branium.base;


import lombok.RequiredArgsConstructor;
import net.branium.daily.DailyWeatherApiController;
import net.branium.full.FullWeatherApiController;
import net.branium.hourly.HourlyWeatherApiController;
import net.branium.location.LocationApiController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import net.branium.realtime.RealtimeWeatherApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    @GetMapping
    public ResponseEntity<RootEntity> handleBaseURI() {
        return ResponseEntity.ok(createRootEntity());
    }

    private RootEntity createRootEntity() {
        RootEntity.RootEntityBuilder rootEntityBuilder = RootEntity.builder();
        rootEntityBuilder.locationsUrl(linkTo(methodOn(LocationApiController.class).listLocations(null, null, null)).toString());
        rootEntityBuilder.locationByCodeUrl(linkTo(methodOn(LocationApiController.class).getLocation(null)).toString());
        rootEntityBuilder.realtimeWeatherByIPUrl(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null)).toString());
        rootEntityBuilder.realtimeWeatherByCodeUrl(linkTo(methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByLocationCode(null)).toString());
        rootEntityBuilder.hourlyForecastByIPUrl(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null)).toString());
        rootEntityBuilder.hourlyForecastByCodeUrl(linkTo(methodOn(HourlyWeatherApiController.class).listHourlyForecastByLocationCode(null, null)).toString());
        rootEntityBuilder.dailyForecastByIPUrl(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByIPAddress(null)).toString());
        rootEntityBuilder.dailyForecastByCodeUrl(linkTo(methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(null)).toString());
        rootEntityBuilder.fullWeatherByIPUrl(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null)).toString());
        rootEntityBuilder.fullWeatherByCodeUrl(linkTo(methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(null)).toString());
        return rootEntityBuilder.build();
    }
}
