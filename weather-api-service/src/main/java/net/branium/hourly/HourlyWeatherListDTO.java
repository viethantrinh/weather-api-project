package net.branium.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"location", "hourly_forecast"})
@Data
@Builder
public class HourlyWeatherListDTO {
    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "hourly_forecast")
    private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>();
}
