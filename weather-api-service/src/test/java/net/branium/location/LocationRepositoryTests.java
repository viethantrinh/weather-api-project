package net.branium.location;

import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import net.branium.common.Location;
import net.branium.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LocationRepositoryTests {

    @Autowired
    LocationRepository locationRepo;

    @Test
    @Rollback(value = false)
    void testCreateOneLocationSuccess() {
        Location location = Location.builder()
                .code("MBMH_IN")
                .cityName("Mumbai")
                .regionName("Maharashtra")
                .countryCode("IN")
                .countryName("India")
                .enabled(true)
                .build();
        Location savedLocation = locationRepo.save(location);
        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("MBMH_IN");
    }

    @Test
    void testGetAllUnTrashedLocationsSuccess() {
        List<Location> locations = locationRepo.findAllUnTrashed();
        assertThat(locations.size()).isEqualTo(2);
    }

    @Test
    void testGetLocationByCodeNotFound() {
        Location location = locationRepo.findByCode("AAA").orElse(null);
        assertThat(location).isNull();
    }

    @Test
    void testGetLocationByCodeFound() {
        Location location = locationRepo.findByCode("NYC_USA").orElse(null);
        assertThat(location).isNotNull();
    }

    @Test
    @Rollback(value = true)
    void testDeleteLocationByCodeSuccess() {
        String code = "NYC_USA";
        locationRepo.deleteByCode(code);
        assertThat(locationRepo.findByCode(code)).isNull();
    }

    @Test
    @Rollback(value = false)
    void testAddRealtimeWeatherData() {
        String code = "DELHI_IN";
        Optional<Location> locationOptional = locationRepo.findByCode(code);
        if (locationOptional.isPresent()) {
            Location location = locationOptional.get();
            RealtimeWeather realtimeWeather = RealtimeWeather.builder()
                    .locationCode(location.getCode())
                    .temperature(60)
                    .humidity(20)
                    .precipitation(7)
                    .windSpeed(5)
                    .status("Sunny")
                    .lastUpdated(LocalDateTime.now())
                    .location(location)
                    .build();
            location.setRealtimeWeather(realtimeWeather);
            Location savedLocation = locationRepo.save(location);

            assertThat(savedLocation).isNotNull();
            assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
            assertThat(savedLocation.getRealtimeWeather()).isNotNull();
            assertThat(savedLocation.getRealtimeWeather().getLocationCode()).isEqualTo("NYC_USA");
            assertThat(savedLocation.getRealtimeWeather().getTemperature()).isEqualTo(30);
            assertThat(savedLocation.getRealtimeWeather().getLocation()).isEqualTo(location);
        }
    }

    @Test
    @Rollback(value = false)
    void testAddHourlyWeatherData() {
        Location location = locationRepo.findByCode("DELHI_IN").orElse(null);

        assert location != null;
        List<HourlyWeather> listHourlyWeather = location.getHourlyWeathers();

        HourlyWeather hourlyForecast1 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(10).build())
                .temperature(15)
                .precipitation(40)
                .status("Sunny")
                .build();

        HourlyWeather hourlyForecast2 = HourlyWeather.builder()
                .id(HourlyWeatherId.builder().location(location).hourOfDay(11).build())
                .temperature(16)
                .precipitation(50)
                .status("Cloudy")
                .build();

        listHourlyWeather.addAll(List.of(hourlyForecast1, hourlyForecast2));

        Location updatedLocation = locationRepo.save(location);
        assertThat(updatedLocation.getHourlyWeathers()).isNotEmpty();
    }


}