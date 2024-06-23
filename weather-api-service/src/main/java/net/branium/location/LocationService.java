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
    final LocationMapper locationMapper;

    public Location createLocation(Location location) {
        return locationRepo.save(location);
    }

    public List<Location> getLocations() {
        return locationRepo.findAllUnTrashed();
    }

    public Location getLocation(String code) {
        return locationRepo.findByCode(code);
    }

    public Location updateLocation(Location location) throws LocationNotFoundException {
        Location locationFromDB = locationRepo.findByCode(location.getCode());
        if (locationFromDB == null) {
            throw new LocationNotFoundException("Can not find location with code: " + location.getCode());
        }
        locationMapper.updateLocationFromRequestLocation(location, locationFromDB);
        return locationRepo.save(locationFromDB);
    }
}
