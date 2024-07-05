package net.branium.location;

import net.branium.common.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
class LocationRepositoryTests {

    @Autowired
    LocationRepository locationRepo;

    @Test
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
    void testDeleteLocationByCodeSuccess() {
        String code = "NYC_USA";
        locationRepo.deleteByCode(code);
        assertThat(locationRepo.findByCode(code)).isEmpty();
    }

    @Test
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
            assertThat(savedLocation.getCode()).isEqualTo("DELHI_IN");
            assertThat(savedLocation.getRealtimeWeather()).isNotNull();
            assertThat(savedLocation.getRealtimeWeather().getLocationCode()).isEqualTo("DELHI_IN");
            assertThat(savedLocation.getRealtimeWeather().getTemperature()).isEqualTo(60);
            assertThat(savedLocation.getRealtimeWeather().getLocation()).isEqualTo(location);
        }
    }

    @Test
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

    @Test
    void testFindByCountryCodeAndCityNameFound() {
        String countryCode = "US";
        String cityName = "New York City";
        Optional<Location> locationOpt = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
        Location location = locationOpt.orElse(null);
        assertThat(location).isNotNull();
    }

    @Test
    void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode = "INVALID_COUNTRY_CODE";
        String cityName = "New York City";
        Optional<Location> locationOpt = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
        Location location = locationOpt.orElse(null);
        assertThat(location).isNull();
    }

    @Test
    void testFindByCountryCodeAndCityNameByTrashedNotFound() {
        String countryCode = "NYC_USA";
        String cityName = "New York City";
        Optional<Location> locationOpt = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
        Location location = locationOpt.orElse(null);
        assertThat(location).isNull();
    }

    @Test
    void testAddDailyWeatherDataSuccess() {
        Location location = locationRepo.findByCode("DELHI_IN").orElse(null);

        assert location != null;
        List<DailyWeather> dailyWeatherList = location.getDailyWeathers();

        DailyWeather dailyWeather1 = DailyWeather
                .builder()
                .id(DailyWeatherId.builder().dayOfMonth(12).month(2).location(location).build())
                .minTemp(20)
                .maxTemp(30)
                .precipitation(40)
                .status("Cloudy")
                .build();


        DailyWeather dailyWeather2 = DailyWeather
                .builder()
                .id(DailyWeatherId.builder().dayOfMonth(15).month(2).location(location).build())
                .minTemp(15)
                .maxTemp(27)
                .precipitation(30)
                .status("Rainy")
                .build();

        dailyWeatherList.addAll(List.of(dailyWeather1, dailyWeather2));

        Location updatedLocation = locationRepo.save(location);
        assertThat(updatedLocation.getHourlyWeathers()).isNotEmpty();
    }

    @Test
    void testDeleteDailyWeatherSuccess() {
        Location location = locationRepo.findByCode("DELHI_IN").orElse(null);
        assert location != null;
        List<DailyWeather> dailyWeatherList = location.getDailyWeathers();
        dailyWeatherList.clear();
        Location updatedLocation = locationRepo.save(location);
        assertThat(updatedLocation.getDailyWeathers()).isEmpty();
    }


}