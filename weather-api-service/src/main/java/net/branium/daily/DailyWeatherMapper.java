package net.branium.daily;

import net.branium.common.DailyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DailyWeatherMapper {

    @Mapping(target = "dayOfMonth", source = "id.dayOfMonth")
    @Mapping(target = "month", source = "id.month")
    DailyWeatherDTO toDailyWeatherDTO(DailyWeather dailyWeather);
}
