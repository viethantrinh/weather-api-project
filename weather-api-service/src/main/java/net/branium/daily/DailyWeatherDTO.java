package net.branium.daily;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@JsonPropertyOrder({"dayOfMonth", "month", "minTemp", "maxTemp", "precipitation", "status"})
@Data
@Builder
public class DailyWeatherDTO {
    @JsonProperty(value = "day_of_month")
    private int dayOfMonth;

    @JsonProperty(value = "month")
    private int month;

    @JsonProperty(value = "min_temp")
    private int minTemp;

    @JsonProperty(value = "max_temp")
    private int maxTemp;

    @JsonProperty(value = "precipitation")
    private int precipitation;

    @JsonProperty(value = "status")
    private String status;
}
