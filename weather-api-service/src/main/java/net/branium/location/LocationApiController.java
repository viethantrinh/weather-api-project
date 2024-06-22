package net.branium.location;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationApiController {
    final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Valid Location location) {
        Location createdLocation = locationService.createLocation(location);
        URI uri = URI.create("/v1/locations/" + createdLocation.getCode());
        return ResponseEntity.created(uri).body(createdLocation);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> locations = locationService.getLocations();
        if (!locations.isEmpty()) {
            return ResponseEntity.ok(locations);
        }
        return ResponseEntity.noContent().build();
    }
}
