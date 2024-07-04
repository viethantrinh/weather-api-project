package net.branium.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@JsonPropertyOrder({"hourOfDay", "temperature", "precipitation", "status"})
@Data
@Builder
public class HourlyWeatherDTO {
    @JsonProperty(value = "hour_of_day")
    private int hourOfDay;

    @JsonProperty(value = "temperature")
    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    @JsonProperty(value = "precipitation")
    private int precipitation;

    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    @NotBlank(message = "Status must not be empty")
    @JsonProperty(value = "status")
    private String status;
}
