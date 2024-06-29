package net.branium.realtime;

import net.branium.common.RealtimeWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface RealtimeWeatherMapper {
    @Mappings({
            @Mapping(target = "location", expression = "java(getLocationStringForRealtimeWeatherDTO(realtimeWeather))")
    })
    RealtimeWeatherDTO toRealtimeWeatherDTO(RealtimeWeather realtimeWeather);


    @Mapping(target = "locationCode", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "lastUpdated", expression = "java(LocalDateTime.now())")
    void updateRealtimeWeatherFromRealtimeWeatherRequest(RealtimeWeather realtimeWeatherRequest,
                                                         @MappingTarget RealtimeWeather realtimeWeatherToUpdate);

    default String getLocationStringForRealtimeWeatherDTO(RealtimeWeather realtimeWeather) {
        return realtimeWeather.getLocation().getCityName() + ", " +
                (realtimeWeather.getLocation().getRegionName() == null ? "" : realtimeWeather.getLocation().getRegionName() + ", ") +
                realtimeWeather.getLocation().getCountryName();
    }
}
