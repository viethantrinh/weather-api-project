package net.branium.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.branium.common.Location;
import net.branium.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/locations")
@RequiredArgsConstructor
@Validated
public class LocationApiController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final Map<String, String> properryMap = Map.ofEntries(
            Map.entry("code", "code"),
            Map.entry("city_name", "cityName"),
            Map.entry("region_name", "regionName"),
            Map.entry("country_code", "countryCode"),
            Map.entry("country_name", "countryName"),
            Map.entry("enabled", "enabled")
    );

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid Location location) {
        Location createdLocation = locationService.createLocation(location);
        LocationDTO locationDTO = locationMapper.toLocationDTO(createdLocation);
        URI uri = URI.create("/v1/locations/" + locationDTO.getCode());
        return ResponseEntity.created(uri).body(locationDTO);
    }

    @GetMapping
    public ResponseEntity<?> listLocations(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Min(value = 1, message = "pageNum is min = 1") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "5") @Max(value = 20, message = "pageSize is max = 20") Integer pageSize,
            @RequestParam(value = "sort", required = false, defaultValue = "code") String sortField
    ) {

        if (!properryMap.containsKey(sortField)) {
            throw new BadRequestException("invalid sort field: " + sortField);
        }

        Page<Location> page = locationService.listByPage(pageNum - 1, pageSize, properryMap.get(sortField));
        List<Location> locations = page.getContent();

        if (!locations.isEmpty()) {
            return ResponseEntity.ok(
                    addPageMetaDataAndLinksToCollection(locations.stream().map(locationMapper::toLocationDTO).toList(), page, sortField)
            );
        }

        return ResponseEntity.noContent().build();
    }

    /**
     * Add page's metadata and first, next, prev links to the get all locations api and
     * @param dtoList location list dto
     * @param pageInfo Page instance of location
     * @return CollectionModel instance of location
     */
    private CollectionModel<LocationDTO> addPageMetaDataAndLinksToCollection(List<LocationDTO> dtoList, Page<Location> pageInfo, String sortField) {
        // add self link to each Location object
        for (LocationDTO dto : dtoList) {
            dto.add(linkTo(methodOn(LocationApiController.class).getLocation(dto.getCode())).withSelfRel());
        }

        int pageNum = pageInfo.getNumber() + 1;
        int pageSize = pageInfo.getSize();
        long totalElements = pageInfo.getTotalElements();
        long totalPages = pageInfo.getTotalPages();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pageSize, pageNum, totalElements, totalPages);
        CollectionModel<LocationDTO> collectionModel = PagedModel.of(dtoList, pageMetadata);

        // add self link to collections
        collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum, pageSize, sortField)).withSelfRel());

        if (pageNum > 1) {
            // add link to first page if the current page is not the first one
            collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(1, pageSize, sortField)).withRel(IanaLinkRelations.FIRST));

            // add link to previous page if the current page is not the first one
            collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum - 1, pageSize, sortField)).withRel(IanaLinkRelations.PREVIOUS));
        }

        if (pageNum < totalPages) {
            // add link to next page if the current page is not the last one
            collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum + 1, pageSize, sortField)).withRel(IanaLinkRelations.NEXT));

            // add link to last page if the current page is not the last one
            collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations((int) totalPages, pageSize, sortField)).withRel(IanaLinkRelations.LAST));
        }

        return collectionModel;
    }

    @Deprecated
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
        Location updatedLocation = locationService.updateLocation(location);
        return ResponseEntity.ok(locationMapper.toLocationDTO(updatedLocation));

    }

    @DeleteMapping(path = "/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {
        locationService.deleteLocation(code);
        return ResponseEntity.noContent().build();
    }
}
