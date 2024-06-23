package net.branium.location;

import net.branium.common.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "code", ignore = true)
    void updateLocationFromRequestLocation(Location requestLocation, @MappingTarget Location location);
}
