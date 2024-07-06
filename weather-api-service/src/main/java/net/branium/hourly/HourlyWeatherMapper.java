package net.branium.hourly;

import net.branium.common.HourlyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface HourlyWeatherMapper {

    @Mapping(target = "hourOfDay", source = "id.hourOfDay")
    HourlyWeatherDTO toHourWeatherDTO(HourlyWeather hourlyWeather);

    @Mapping(target = "id.hourOfDay", source = "hourOfDay")
    HourlyWeather toHourWeather(HourlyWeatherDTO hourlyWeatherDTO);
}
