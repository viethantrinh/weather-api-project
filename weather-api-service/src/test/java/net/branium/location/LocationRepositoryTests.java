package net.branium.location;

import net.branium.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

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
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .build();
        Location savedLocation = locationRepo.save(location);
        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
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
}