package net.branium.daily;

import net.branium.common.DailyWeather;
import net.branium.common.DailyWeatherId;
import net.branium.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = true)
class DailyWeatherRepositoryTests {

    @Autowired DailyWeatherRepository dailyWeatherRepo;

    @Test
    void testAddDailyWeatherDataSuccess() {

        Location location = Location
                .builder()
                .code("DELHI_IN")
                .build();

        DailyWeather dailyWeather1 = DailyWeather
                .builder()
                .id(DailyWeatherId.builder().dayOfMonth(11).month(2).location(location).build())
                .minTemp(20)
                .maxTemp(30)
                .precipitation(40)
                .status("Cloudy")
                .build();


        DailyWeather dailyWeather2 = DailyWeather
                .builder()
                .id(DailyWeatherId.builder().dayOfMonth(10).month(2).location(location).build())
                .minTemp(15)
                .maxTemp(27)
                .precipitation(30)
                .status("Rainy")
                .build();

        Iterable<DailyWeather> dailyWeathers = dailyWeatherRepo.saveAll(List.of(dailyWeather1, dailyWeather2));
        assertThat(dailyWeathers).isNotEmpty();
    }

    @Test
    void testDeleteDailyWeatherSuccess() {
        Location location = Location
                .builder()
                .code("DELHI_IN")
                .build();

        DailyWeather dailyWeather1 = DailyWeather
                .builder()
                .id(DailyWeatherId.builder().dayOfMonth(11).month(2).location(location).build())
                .minTemp(20)
                .maxTemp(30)
                .precipitation(40)
                .status("Cloudy")
                .build();

        dailyWeatherRepo.delete(dailyWeather1);
        DailyWeather dailyWeatherDeleted = dailyWeatherRepo.findById(dailyWeather1.getId()).orElse(null);
        assertThat(dailyWeatherDeleted).isNull();
    }

    @Test
    void testFindByLocationCodeFound() {
        List<DailyWeather> dailyWeatherList = dailyWeatherRepo.findByLocationCode("DELHI_IN");
        System.out.println(dailyWeatherList);
        assertThat(dailyWeatherList).isNotEmpty();
    }

    @Test
    void testFindByLocationCodeNotFound() {
        List<DailyWeather> dailyWeatherList = dailyWeatherRepo.findByLocationCode("INVALID_LOCATION_CODE");
        System.out.println(dailyWeatherList);
        assertThat(dailyWeatherList).isEmpty();
    }
}