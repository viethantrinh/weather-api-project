package net.branium.location;

import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import net.branium.exception.LocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    final LocationRepository locationRepo;
    final LocationMapper locationMapper;

    public Location createLocation(Location location) {
        return locationRepo.save(location);
    }

    public Page<Location> listByPage(int pageNum, int pageSize, String sortField) {
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        return locationRepo.findAllUnTrashed(pageable);
    }

    @Deprecated
    public List<Location> getLocations() {
        return locationRepo.findAllUnTrashed();
    }

    public Location getLocation(String code) {
        return locationRepo.findByCode(code).orElseThrow(() -> new LocationNotFoundException(code));
    }

    public Location updateLocation(Location location) {
        Location locationFromDB = locationRepo.findByCode(location.getCode())
                .orElseThrow(() -> new LocationNotFoundException(location.getCode()));
        locationMapper.updateLocationFromRequestLocation(location, locationFromDB);
        return locationRepo.save(locationFromDB);
    }


    public void deleteLocation(String code) {
        locationRepo.findByCode(code)
                .orElseThrow(() -> new LocationNotFoundException(code));
        locationRepo.deleteByCode(code);
    }
}
