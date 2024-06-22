package net.branium.location;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationService {
    final LocationRepository locationRepo;

    public Location createLocation(Location location) {
        return locationRepo.save(location);
    }

    public List<Location> getLocations() {
        return locationRepo.findAllUnTrashedLocation();
    }
}
