package net.branium.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.branium.GeolocationException;
import net.branium.GeolocationService;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.LocationNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RealtimeWeatherApiController.class)
class RealtimeWeatherApiControllerTests {
    private static final String END_POINT_PATH = "/v1/realtime";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RealtimeWeatherMapper realtimeWeatherMapper;

    @MockBean
    RealtimeWeatherService realtimeWeatherService;

    @MockBean
    GeolocationService geolocationService;



    @Test
    void givenInvalidClientIP_whenGetRealtimeWeatherByIPAddressCalled_thenShouldReturn404BadRequest() throws Exception {
        when(geolocationService.getLocation(anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void givenNotAvailableRealtimeWeather_whenGetRealtimeWeatherByIPAddressCalled_thenShouldReturn404NotFound() throws Exception {
        Location location = Location.builder().build();
        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(realtimeWeatherService.getRealtimeWeatherByLocationCountryCodeAndCityName(any(Location.class))).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenValidClientIPAndAvailableRealtimeWeather_whenGetRealtimeWeatherByIPAddressCalled_thenShouldReturn200OK() throws Exception {
        String ipAddress = "108.30.178.78"; // new york ip address
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .trashed(false)
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .locationCode(location.getCode())
                .temperature(30)
                .humidity(20)
                .precipitation(40)
                .windSpeed(2)
                .status("Sunny")
                .lastUpdated(LocalDateTime.now())
                .location(location)
                .build();

        location.setRealtimeWeather(realtimeWeather);

        RealtimeWeatherDTO dto = RealtimeWeatherDTO.builder()
                .location(realtimeWeather.getLocation().getCityName() + ", "
                        + realtimeWeather.getLocation().getRegionName() + ", "
                        + realtimeWeather.getLocation().getCountryName()
                )
                .temperature(realtimeWeather.getTemperature())
                .humidity(realtimeWeather.getHumidity())
                .precipitation(realtimeWeather.getPrecipitation())
                .windSpeed(realtimeWeather.getWindSpeed())
                .status(realtimeWeather.getStatus())
                .lastUpdated(realtimeWeather.getLastUpdated())
                .build();

        when(geolocationService.getLocation(anyString())).thenReturn(location);
        when(realtimeWeatherService.getRealtimeWeatherByLocationCountryCodeAndCityName(any(Location.class))).thenReturn(realtimeWeather);
        when(realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather)).thenReturn(dto);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName())))
                .andDo(print());
    }


    @Test
    void givenNotAvailableLocationCode_whenGetRealtimeWeatherByLocationCodeCalled_thenShouldReturn404NotFound() throws Exception {
        when(realtimeWeatherService.getRealtimeWeatherByLocationCode(anyString())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH + "/NON_EXISTS_LOCATION_CODE"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenAvailableLocationCode_whenGetRealtimeWeatherByLocationCodeCalled_thenShouldReturn200OK() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .trashed(false)
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .locationCode(location.getCode())
                .temperature(30)
                .humidity(20)
                .precipitation(40)
                .windSpeed(2)
                .status("Sunny")
                .lastUpdated(LocalDateTime.now())
                .location(location)
                .build();

        location.setRealtimeWeather(realtimeWeather);

        RealtimeWeatherDTO dto = RealtimeWeatherDTO.builder()
                .location(realtimeWeather.getLocation().getCityName() + ", "
                        + realtimeWeather.getLocation().getRegionName() + ", "
                        + realtimeWeather.getLocation().getCountryName()
                )
                .temperature(realtimeWeather.getTemperature())
                .humidity(realtimeWeather.getHumidity())
                .precipitation(realtimeWeather.getPrecipitation())
                .windSpeed(realtimeWeather.getWindSpeed())
                .status(realtimeWeather.getStatus())
                .lastUpdated(realtimeWeather.getLastUpdated())
                .build();

        when(realtimeWeatherService.getRealtimeWeatherByLocationCode(anyString())).thenReturn(realtimeWeather);
        when(realtimeWeatherMapper.toRealtimeWeatherDTO(realtimeWeather)).thenReturn(dto);

        mockMvc.perform(get(END_POINT_PATH + "/" + location.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName())))
                .andDo(print());
    }

    @Test
    void givenNotAvailableLocationCode_whenUpdateRealtimeWeatherByLocationCodeCalled_thenShouldReturn404NotFound() throws Exception {
        RealtimeWeather realtimeWeatherRequest = RealtimeWeather.builder()
                .temperature(30)
                .humidity(20)
                .precipitation(40)
                .windSpeed(2)
                .status("Sunny")
                .build();

        String jsonBody = objectMapper.writeValueAsString(realtimeWeatherRequest);

        when(realtimeWeatherService.updateRealtimeWeather(anyString(), any(RealtimeWeather.class))).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(put(END_POINT_PATH + "/NON_EXISTS_LOCATION_CODE")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void givenAvailableLocationCodeAndInvalidFieldRealtimeWeatherRequest_whenUpdateRealtimeWeatherByLocationCodeCalled_thenShouldReturn400BadRequest() throws Exception {
        String locationCode = "NYC_USA";
        RealtimeWeather realtimeWeatherRequest = RealtimeWeather.builder()
                .temperature(-60)
                .humidity(20)
                .precipitation(40)
                .windSpeed(2)
                .status("Sunny")
                .build();

        String jsonBody = objectMapper.writeValueAsString(realtimeWeatherRequest);

        when(realtimeWeatherService.updateRealtimeWeather(anyString(), any(RealtimeWeather.class))).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(put(END_POINT_PATH + "/" + locationCode)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is("Temperature must be in the range of -50 to 50 Celsius degree")))
                .andDo(print());
    }

    @Test
    void givenAvailableLocationCodeAndValidFieldRealtimeWeatherRequest_whenUpdateRealtimeWeatherByLocationCodeCalled_thenShouldReturn200OK() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .trashed(false)
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .locationCode(location.getCode())
                .temperature(10)
                .humidity(10)
                .precipitation(10)
                .windSpeed(10)
                .status("Sunny")
                .lastUpdated(LocalDateTime.of(2024, 10, 10, 10, 10, 10))
                .location(location)
                .build();

        location.setRealtimeWeather(realtimeWeather);

        RealtimeWeather realtimeWeatherRequest = RealtimeWeather.builder()
                .temperature(5)
                .humidity(5)
                .precipitation(5)
                .windSpeed(5)
                .status("Sunny")
                .location(location)
                .build();

//        location.setRealtimeWeather(realtimeWeatherRequest);
//
//
//        String jsonBody = objectMapper.writeValueAsString(realtimeWeatherRequest);
//
//        when(realtimeWeatherService.updateRealtimeWeather("NYC_USA", realtimeWeatherRequest)).thenReturn(realtimeWeatherRequest);
//
//        mockMvc.perform(put(END_POINT_PATH + "/" + location.getCode())
//                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]", is("Temperature must be in the range of -50 to 50 Celsius degree")))
//                .andDo(print());
    }


}