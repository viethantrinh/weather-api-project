package net.branium.realtime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.GeolocationService;
import net.branium.util.Utilities;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(realtimeWeatherDTO);
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocationCode(locationCode);
        RealtimeWeatherDTO realtimeWeatherDTO = realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather);
        return ResponseEntity.ok(realtimeWeatherDTO);
    }

    @PutMapping(path = "/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode,
                                                                 @RequestBody @Valid RealtimeWeather realtimeWeatherRequest) {
        RealtimeWeather updatedRealtimeWeather = realtimeWeatherService
                .updateRealtimeWeather(locationCode, realtimeWeatherRequest);
        RealtimeWeatherDTO dto = realtimeWeatherMapper.toRealtimeWeatherDTO(updatedRealtimeWeather);
        return ResponseEntity.ok(dto);

    }

}
