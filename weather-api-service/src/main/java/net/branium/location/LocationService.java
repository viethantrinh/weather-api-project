package net.branium.location;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LocationService {
    final LocationRepository locationRepo;
    final LocationMapper locationMapper;

    @Autowired
    public LocationService(LocationRepository locationRepo, LocationMapper locationMapper) {
        this.locationRepo = locationRepo;
        this.locationMapper = locationMapper;
    }

    public Location createLocation(Location location) {
        return locationRepo.save(location);
    }

    public List<Location> getLocations() {
        return locationRepo.findAllUnTrashed();
    }

    public Location getLocation(String code) {
        return locationRepo.findByCode(code).orElse(null);
    }

    public Location updateLocation(Location location) throws Exception {
        Location locationFromDB = locationRepo.findByCode(location.getCode())
                .orElseThrow(() -> new LocationNotFoundException("Can not find location with code: " + location.getCode()));
        locationMapper.updateLocationFromRequestLocation(location, locationFromDB);
        return locationRepo.save(locationFromDB);
    }


    public void deleteLocation(String code) throws LocationNotFoundException {
        locationRepo.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException("Can not find location with code: " + code));
        locationRepo.deleteByCode(code);
    }
}
