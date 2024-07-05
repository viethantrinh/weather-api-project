package net.branium.daily;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"location", "daily_forecast"})
@Data
@Builder
public class DailyWeatherListDTO {
    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "daily_forecast")
    private List<DailyWeatherDTO> dailyWeatherDTOList;
}
