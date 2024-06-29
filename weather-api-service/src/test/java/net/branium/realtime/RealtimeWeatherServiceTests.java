package net.branium.realtime;

import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import net.branium.location.LocationNotFoundException;
import net.branium.location.LocationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(SpringExtension.class)
class RealtimeWeatherServiceTests {

    @Mock
    RealtimeWeatherRepository realtimeWeatherRepo;

    @Mock
    LocationRepository locationRepository;

    @Mock
    RealtimeWeatherMapperImpl realtimeWeatherMapperImpl;

    @InjectMocks
    RealtimeWeatherService realtimeWeatherService;

    @BeforeEach
    void setUp() {
        openMocks(realtimeWeatherMapperImpl);
    }

    @Test
    void updateRealtimeWeather_LocationNotHaveManagedRealtimeWeather() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .countryName("United State of America")
                .countryCode("US")
                .regionName("New York")
                .cityName("New York City")
                .enabled(true)
                .trashed(false)
                .build();

        RealtimeWeather realtimeWeatherRequest = RealtimeWeather.builder()
                .temperature(30)
                .humidity(20)
                .status("Sunny")
                .precipitation(22)
                .windSpeed(3)
                .build();

        Location savedLocation = Location.builder()
                .code("NYC_USA")
                .countryName("United State of America")
                .countryCode("US")
                .regionName("New York")
                .cityName("New York City")
                .enabled(true)
                .trashed(false)
                .realtimeWeather(realtimeWeatherRequest)
                .build();

        when(locationRepository.findByCode(anyString())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        RealtimeWeather realtimeWeather = realtimeWeatherService.updateRealtimeWeather(location.getCode(), realtimeWeatherRequest);

        assertEquals("NYC_USA", realtimeWeather.getLocationCode());
    }

    @Test
    void updateRealtimeWeather_LocationHaveManagedRealtimeWeather() throws Exception {
        Location locationAssociatedWithRealtimeWeather = Location.builder()
                .code("NYC_USA")
                .countryName("United State of America")
                .countryCode("US")
                .regionName("New York")
                .cityName("New York City")
                .enabled(true)
                .trashed(false)
                .build();

        RealtimeWeather realtimeWeatherAssociatedWithLocation = RealtimeWeather.builder()
                .locationCode(locationAssociatedWithRealtimeWeather.getCode())
                .temperature(5)
                .humidity(5)
                .status("Rainy")
                .precipitation(5)
                .windSpeed(5)
                .lastUpdated(LocalDateTime.of(2024, 10, 10, 10, 10, 10))
                .location(locationAssociatedWithRealtimeWeather)
                .build();

        locationAssociatedWithRealtimeWeather.setRealtimeWeather(realtimeWeatherAssociatedWithLocation);

        RealtimeWeather realtimeWeatherRequest = RealtimeWeather.builder()
                .temperature(10)
                .humidity(10)
                .status("Sunny")
                .precipitation(10)
                .windSpeed(10)
                .build();



        when(locationRepository.findByCode("NYC_USA")).thenReturn(Optional.of(locationAssociatedWithRealtimeWeather));
        doCallRealMethod().when(realtimeWeatherMapperImpl).updateRealtimeWeatherFromRealtimeWeatherRequest(realtimeWeatherRequest, realtimeWeatherAssociatedWithLocation);
        when(realtimeWeatherRepo.save(realtimeWeatherAssociatedWithLocation)).thenReturn(locationAssociatedWithRealtimeWeather.getRealtimeWeather());

        RealtimeWeather realtimeWeather = realtimeWeatherService
                .updateRealtimeWeather("NYC_USA", realtimeWeatherRequest);

        System.out.println(realtimeWeather);

        verify(locationRepository, times(0)).save(any(Location.class));
        verify(realtimeWeatherMapperImpl, times(1)).updateRealtimeWeatherFromRealtimeWeatherRequest(realtimeWeatherRequest, realtimeWeatherAssociatedWithLocation);
        verify(realtimeWeatherRepo, times(1)).save(any(RealtimeWeather.class));
    }
}