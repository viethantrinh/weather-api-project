package net.branium.hourly;

import net.branium.common.HourlyWeather;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface HourlyWeatherMapper {

    @Mapping(target = "hourOfDay", source = "id.hourOfDay")
    HourlyWeatherDTO toHourWeatherDTO(HourlyWeather hourlyWeather);

    @Mapping(target = "id.hourOfDay",
            expression = "java(hourlyWeatherDTO.getHourOfDay() != 0 ? hourlyWeatherDTO.getHourOfDay() : 0)")
    HourlyWeather toHourWeather(HourlyWeatherDTO hourlyWeatherDTO);
}
