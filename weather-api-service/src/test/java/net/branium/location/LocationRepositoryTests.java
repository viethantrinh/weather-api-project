package net.branium.location;

import net.branium.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LocationRepositoryTests {

    @Autowired
    LocationRepository locationRepo;

    @Test
    @Rollback(value = false)
    void testAddSuccess() {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .build();
        Location savedLocation = locationRepo.save(location);
        assertNotNull(savedLocation);
        assertEquals(savedLocation.getCode(), "NYC_USA");
    }
}