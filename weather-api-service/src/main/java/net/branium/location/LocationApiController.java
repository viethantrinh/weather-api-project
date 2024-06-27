package net.branium.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {
    private final LocationService locationService;

    @Autowired
    public LocationApiController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Valid Location location) {
        Location createdLocation = locationService.createLocation(location);
        URI uri = URI.create("/v1/locations/" + createdLocation.getCode());
        return ResponseEntity.created(uri).body(createdLocation);
    }

    @GetMapping
    public ResponseEntity<?> getLocations() {
        List<Location> locations = locationService.getLocations();
        if (!locations.isEmpty()) {
            return ResponseEntity.ok(locations);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
        Location location = locationService.getLocation(code);
        if (location != null) {
            return ResponseEntity.ok(location);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location) {
        try {
            Location updatedLocation = locationService.updateLocation(location);
            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {
        try {
            locationService.deleteLocation(code);
            return ResponseEntity.noContent().build();
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
