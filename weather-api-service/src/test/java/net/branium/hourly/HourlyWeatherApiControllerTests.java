package net.branium.hourly;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.branium.GeolocationException;
import net.branium.GeolocationService;
import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import net.branium.common.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HourlyWeatherApiController.class)
@ComponentScan({"net.branium.hourly"})
class HourlyWeatherApiControllerTests {
    private static final String END_POINT_PATH = "/v1/hourly";
    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    private static final String X_CURRENT_HOUR = "X-Current-Hour";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    HourlyWeatherService hourlyWeatherService;
    @MockBean
    GeolocationService geolocationService;
    @Autowired
    HourlyWeatherMapper hourlyWeatherMapper;


    @Test
    void testGetByIPShouldReturn400BadRequestBecauseHeaderXCurrentHourNotValid() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_FORWARDED_FOR, "103.48.198.141");
        httpHeaders.add(X_CURRENT_HOUR, "");

        mockMvc.perform(get(END_POINT_PATH).headers(httpHeaders))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_FORWARDED_FOR, "103.48.198...");
        httpHeaders.add(X_CURRENT_HOUR, "9");

        when(geolocationService.getLocation(anyString())).thenThrow(GeolocationException.class);

        mockMvc.perform(get(END_POINT_PATH).headers(httpHeaders))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetByIPShouldReturn204NoContent() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_FORWARDED_FOR, "103.48.198.141");
        httpHeaders.add(X_CURRENT_HOUR, "9");

        Location location = Location.builder()
                .code("DELHI_IN")
                .cityName("New Delhi")
                .regionName("Delhi")
                .countryCode("IN")
                .countryName("INDIA")
                .enabled(true)
                .hourlyWeathers(Collections.emptyList())
                .build();

        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(hourlyWeatherService.getHourlyWeatherByLocationAndCurrentHour(location, 9)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH).headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testGetByIPShouldReturn200OK() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_FORWARDED_FOR, "103.48.198.141");
        httpHeaders.add(X_CURRENT_HOUR, "9");


        Location location = Location.builder()
                .code("DELHI_IN")
                .cityName("New Delhi")
                .regionName("Delhi")
                .countryCode("IN")
                .countryName("INDIA")
                .enabled(true)
                .build();

        HourlyWeather hourlyForecast1 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(10).build())
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeather hourlyForecast2 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(11).build())
                .temperature(13)
                .precipitation(70)
                .status("Sunny")
                .build();

        HourlyWeather hourlyForecast3 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(12).build())
                .temperature(13)
                .precipitation(70)
                .status("Rainy")
                .build();

        location.setHourlyWeathers(List.of(hourlyForecast1, hourlyForecast2, hourlyForecast3));


        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(hourlyWeatherService.getHourlyWeatherByLocationAndCurrentHour(location, 9)).thenReturn(location.getHourlyWeathers());

        mockMvc.perform(get(END_POINT_PATH).headers(httpHeaders))
                .andExpect(status().isOk())
                .andDo(print());
    }

}