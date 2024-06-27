package net.branium.realtime;

import net.branium.common.RealtimeWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RealtimeWeatherMapper {
    @Mappings({
            @Mapping(target = "location", expression = "java(getLocationStringForRealtimeWeatherDTO(realtimeWeather))")
    })
    RealtimeWeatherDTO toRealtimeWeatherDTO(RealtimeWeather realtimeWeather);


    default String getLocationStringForRealtimeWeatherDTO(RealtimeWeather realtimeWeather) {
        return realtimeWeather.getLocation().getCityName() + ", " +
                realtimeWeather.getLocation().getRegionName() + ", " +
                realtimeWeather.getLocation().getCountryName();
    }
}
