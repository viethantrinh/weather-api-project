package net.branium.location;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
@RequiredArgsConstructor
public class LocationApiController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid Location location) {
        Location createdLocation = locationService.createLocation(location);
        LocationDTO locationDTO = locationMapper.toLocationDTO(createdLocation);
        URI uri = URI.create("/v1/locations/" + locationDTO.getCode());
        return ResponseEntity.created(uri).body(locationDTO);
    }

    @GetMapping
    public ResponseEntity<?> getLocations() {
        List<Location> locations = locationService.getLocations();
        if (!locations.isEmpty()) {
            return ResponseEntity.ok(locations.stream().map(locationMapper::toLocationDTO).toList());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
        Location location = locationService.getLocation(code);
        if (location != null) {
            return ResponseEntity.ok(locationMapper.toLocationDTO(location));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location) {
        try {
            Location updatedLocation = locationService.updateLocation(location);
            return ResponseEntity.ok(locationMapper.toLocationDTO(updatedLocation));
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
