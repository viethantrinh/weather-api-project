package net.branium.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.exception.GeolocationException;
import net.branium.exception.LocationNotFoundException;
import net.branium.location.GeolocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RealtimeWeatherApiController.class)
@ComponentScan(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = RealtimeWeatherMapperImpl.class),
        useDefaultFilters = false
)
class RealtimeWeatherApiControllerTests {
    private static final String END_POINT_PATH = "/v1/realtime";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
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

        mockMvc.perform(get(END_POINT_PATH + "/" + location.getCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location", is(location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName())))
                .andDo(print());
    }

    @Test
    void givenNotAvailableLocationCode_whenUpdateRealtimeWeatherByLocationCodeCalled_thenShouldReturn404NotFound() throws Exception {
        RealtimeWeatherDTO realtimeWeatherRequest = RealtimeWeatherDTO.builder()
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


        RealtimeWeatherDTO realtimeWeatherDTO = RealtimeWeatherDTO.builder()
                .temperature(10)
                .humidity(10)
                .precipitation(10)
                .windSpeed(10)
                .status("Rainy")
                .build();

        RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                .temperature(realtimeWeatherDTO.getTemperature())
                .humidity(realtimeWeatherDTO.getHumidity())
                .precipitation(realtimeWeatherDTO.getPrecipitation())
                .windSpeed(realtimeWeatherDTO.getWindSpeed())
                .status(realtimeWeatherDTO.getStatus())
                .location(location)
                .lastUpdated(LocalDateTime.now())
                .build();

        String jsonBody = objectMapper.writeValueAsString(realtimeWeatherDTO);

        when(realtimeWeatherService.updateRealtimeWeather(anyString(), any(RealtimeWeather.class))).thenReturn(realtimeWeather);

        mockMvc.perform(put(END_POINT_PATH + "/" + location.getCode())
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());
    }


}