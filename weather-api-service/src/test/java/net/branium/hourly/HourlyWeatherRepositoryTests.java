package net.branium.hourly;

import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import net.branium.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HourlyWeatherRepositoryTests {

    @Autowired HourlyWeatherRepository hourlyWeatherRepo;

    @Test
    @Rollback(value = false)
    void testAddHourlyWeatherSuccessfully() {
        String code = "DELHI_IN";
        int hourOfDay = 12;

        Location location = Location.builder()
                .code(code)
                .build();

        HourlyWeather hourlyForecast1 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(hourOfDay).build())
                .temperature(13)
                .precipitation(70)
                .status("Cloudy")
                .build();

        HourlyWeather savedHourlyForecast = hourlyWeatherRepo.save(hourlyForecast1);
        assertEquals(hourOfDay, savedHourlyForecast.getId().getHourOfDay());
        assertEquals(code, savedHourlyForecast.getId().getLocation().getCode());
    }

    @Test
    @Rollback(value = false)
    void testDeleteHourlyWeatherSuccessfully() {
        String code = "DELHI_IN";
        int hourOfDay = 12;

        Location location = Location.builder()
                .code(code)
                .build();

        HourlyWeatherId hourlyWeatherId = HourlyWeatherId.builder()
                .location(location)
                .hourOfDay(hourOfDay)
                .build();

        hourlyWeatherRepo.deleteById(hourlyWeatherId);

        HourlyWeather hourlyWeather = hourlyWeatherRepo.findById(hourlyWeatherId).orElse(null);
        assertNull(hourlyWeather);
    }

    @Test
    void findByLocationCodeFound() {
        String locationCode = "DELHI_IN";
        int currentHour = 10;
        List<HourlyWeather> listHourlyForecast = hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationCode, currentHour);
        System.out.println(listHourlyForecast);
        assertFalse(listHourlyForecast.isEmpty());
    }

    @Test
    void findByLocationCodeNotFound() {
        String locationCode = "DELHI_IN";
        int currentHour = 15;
        List<HourlyWeather> listHourlyForecast = hourlyWeatherRepo.findByLocationCodeAndCurrentHour(locationCode, currentHour);
        System.out.println(listHourlyForecast);
        assertTrue(listHourlyForecast.isEmpty());
    }
}