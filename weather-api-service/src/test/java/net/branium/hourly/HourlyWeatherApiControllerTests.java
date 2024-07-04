package net.branium.hourly;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import net.branium.common.Location;
import net.branium.exception.GeolocationException;
import net.branium.exception.LocationNotFoundException;
import net.branium.location.GeolocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HourlyWeatherApiController.class)
@ComponentScan(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = HourlyWeatherMapperImpl.class),
        useDefaultFilters = false  // This will disable the default behavior of scanning all components
)
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

    @Test
    void testGetByLocationCodeShouldReturn400BadRequestBecauseHeaderXCurrentHourNotValid() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_CURRENT_HOUR, "");
        String locationCode = "DELHI_IN";

        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).headers(httpHeaders))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetByLocationCodeShouldReturn404NotFound() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_CURRENT_HOUR, "9");
        String locationCode = "NYC_USA";
        when(hourlyWeatherService.getHourlyWeatherByLocationCodeAndCurrentHour(anyString(), anyInt()))
                .thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).headers(httpHeaders))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testGetByLocationCodeShouldReturn204NoContent() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(X_CURRENT_HOUR, "9");
        String locationCode = "NYC_USA";

        when(hourlyWeatherService.getHourlyWeatherByLocationCodeAndCurrentHour(anyString(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH + "/" + locationCode).headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void testGetByLocationCodeShouldReturn200OK() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
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


        when(hourlyWeatherService.getHourlyWeatherByLocationCodeAndCurrentHour(location.getCode(), 9))
                .thenReturn(location.getHourlyWeathers());

        mockMvc.perform(get(END_POINT_PATH + "/" + location.getCode()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
        String requestUrl = END_POINT_PATH + "/NYC_USA";
        List<HourlyWeatherDTO> hourlyWeatherDTOList = new ArrayList<>();
        String jsonBody = objectMapper.writeValueAsString(hourlyWeatherDTOList);
        mockMvc.perform(put(requestUrl).contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn400BadRequestBecauseInvalidData() throws Exception {
        String requestUrl = END_POINT_PATH + "/NYC_USA";
        List<HourlyWeatherDTO> hourlyWeatherDTOList = new ArrayList<>();

        HourlyWeatherDTO hourlyWeatherDTO1 = HourlyWeatherDTO.builder()
                .hourOfDay(-1)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeatherDTO hourlyWeatherDTO2 = HourlyWeatherDTO.builder()
                .hourOfDay(10)
                .temperature(100)
                .precipitation(220)
                .status("Rainy")
                .build();

        HourlyWeatherDTO hourlyWeatherDTO3 = HourlyWeatherDTO.builder()
                .hourOfDay(12)
                .temperature(13)
                .precipitation(70)
                .status("Rainy")
                .build();

        hourlyWeatherDTOList.addAll(List.of(hourlyWeatherDTO1, hourlyWeatherDTO2, hourlyWeatherDTO3));

        String jsonBody = objectMapper.writeValueAsString(hourlyWeatherDTOList);

        mockMvc.perform(put(requestUrl).contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testUpdateShouldReturn200OK() throws Exception {
        String requestUrl = END_POINT_PATH + "/DELHI_IN";

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

        HourlyWeatherDTO hourlyWeatherDTO1 = HourlyWeatherDTO.builder()
                .hourOfDay(10)
                .temperature(10)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeatherDTO hourlyWeatherDTO2 = HourlyWeatherDTO.builder()
                .hourOfDay(11)
                .temperature(10)
                .precipitation(20)
                .status("Sunny")
                .build();

        HourlyWeatherDTO hourlyWeatherDTO3 = HourlyWeatherDTO.builder()
                .hourOfDay(12)
                .temperature(13)
                .precipitation(70)
                .status("Rainy")
                .build();

        List<HourlyWeatherDTO> hourlyWeatherDTOList = new ArrayList<>(List.of(hourlyWeatherDTO1, hourlyWeatherDTO2, hourlyWeatherDTO3));

        String jsonBody = objectMapper.writeValueAsString(hourlyWeatherDTOList);

        when(hourlyWeatherService.updateHourlyWeatherByLocationCode(anyString(), anyList()))
                .thenReturn(location.getHourlyWeathers());

        mockMvc.perform(put(requestUrl).contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());
    }



}