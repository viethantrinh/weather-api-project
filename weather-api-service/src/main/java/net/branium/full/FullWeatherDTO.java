package net.branium.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.branium.daily.DailyWeatherDTO;
import net.branium.hourly.HourlyWeatherDTO;
import net.branium.realtime.RealtimeWeatherDTO;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"location", "realtimeWeather", "hourlyWeather", "dailyWeather"})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullWeatherDTO {

    @JsonProperty("location")
    private String location;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = RealtimeWeatherFieldFilter.class)
    @JsonProperty("realtime_weather")
    @Valid
    private RealtimeWeatherDTO realtimeWeather;

    @JsonProperty("hourly_forecast")
    @Valid
    private List<HourlyWeatherDTO> hourlyWeather = new ArrayList<>();

    @JsonProperty("daily_forecast")
    @Valid
    private List<DailyWeatherDTO> dailyWeather = new ArrayList<>();
}
