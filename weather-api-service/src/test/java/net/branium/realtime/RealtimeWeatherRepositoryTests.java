package net.branium.realtime;

import net.branium.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class RealtimeWeatherRepositoryTests {

    @Autowired
    RealtimeWeatherRepository realtimeWeatherRepo;

    @Test
    void testFindByCountryCodeAndCityNameFound() {
        String countryCode = "US";
        String cityName = "New York City";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName).get();

        assertAll(
                () -> assertNotNull(realtimeWeather),
                () -> assertNotNull(realtimeWeather.getLocation()),
                () -> assertEquals("NYC_USA", realtimeWeather.getLocationCode())
        );
    }

    @Test
    void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "US";
        String cityName = "Los Angeles";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCountryCodeAndCity(countryCode, cityName).orElse(null);
    }

    @Test
    void testUpdateRealtimeWeatherSuccessful() {
        String realtimeWeatherLocationCode = "DELHI_IN";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findById(realtimeWeatherLocationCode).orElse(null);
        if (realtimeWeather != null) {
            realtimeWeather.setTemperature(20);
            realtimeWeather.setHumidity(20);
            realtimeWeather.setLastUpdated(LocalDateTime.now());
            realtimeWeather.setStatus("Rainy");
            RealtimeWeather updatedRealtimeWeather = realtimeWeatherRepo.save(realtimeWeather);

            assertAll(
                    () -> assertNotNull(updatedRealtimeWeather),
                    () -> assertEquals(20, updatedRealtimeWeather.getHumidity()),
                    () -> assertEquals("Rainy", updatedRealtimeWeather.getStatus())
            );
        }
    }

    @Test
    void testFindByLocationCodeNotFound() {
        String locationCode = "INVALID_LOCATION_CODE";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode).orElse(null);
        assertNull(realtimeWeather);
    }

    @Test
    void testFindByTrashedLocationNotFound() {
        String locationCode = "NYC_USA";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode).orElse(null);
    }

    @Test
    void testFindByLocationCodeFound() {
        String locationCode = "NYC_USA";
        RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode).orElse(null);
        assertNotNull(realtimeWeather);
        assertNotNull(realtimeWeather.getLocation());
        assertEquals("New York City" ,realtimeWeather.getLocation().getCityName());
    }
}