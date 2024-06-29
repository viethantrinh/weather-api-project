package net.branium.realtime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.branium.GeolocationException;
import net.branium.GeolocationService;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.LocationNotFoundException;
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
        try {
            Location locationFromIP = geolocationService.getLocation(clientIPAddress);
            RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocationCountryCodeAndCityName(locationFromIP);
            RealtimeWeatherDTO realtimeWeatherDTO = realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather);
            return ResponseEntity.ok(realtimeWeatherDTO);
        } catch (LocationNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        } catch (GeolocationException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {
        try {
            RealtimeWeather realtimeWeather = realtimeWeatherService.getRealtimeWeatherByLocationCode(locationCode);
            RealtimeWeatherDTO realtimeWeatherDTO = realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather);
            return ResponseEntity.ok(realtimeWeatherDTO);
        } catch (LocationNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode,
                                                                 @RequestBody @Valid RealtimeWeather realtimeWeatherRequest) {
        try {
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherService
                    .updateRealtimeWeather(locationCode, realtimeWeatherRequest);
            RealtimeWeatherDTO dto = realtimeWeatherMapper.toRealtimeWeatherDTO(updatedRealtimeWeather);
            return ResponseEntity.ok(dto);
        } catch (LocationNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

}
