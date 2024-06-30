package net.branium.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@JsonPropertyOrder({"hourOfDay", "temperature", "precipitation", "status"})
@Data
@Builder
public class HourlyWeatherDTO {
    @JsonProperty(value = "hour_of_day")
    private String hourOfDay;

    @JsonProperty(value = "temperature")
    private String temperature;

    @JsonProperty(value = "precipitation")
    private String precipitation;

    @JsonProperty(value = "status")
    private String status;
}
