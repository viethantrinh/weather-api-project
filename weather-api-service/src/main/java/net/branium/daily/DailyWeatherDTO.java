package net.branium.daily;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@JsonPropertyOrder({"dayOfMonth", "month", "minTemp", "maxTemp", "precipitation", "status"})
@Data
@Builder
public class DailyWeatherDTO {
    @JsonProperty(value = "day_of_month")
    @Range(min = 1, max = 31, message = "The forecasted day must be between 1 and 31")
    private int dayOfMonth;

    @JsonProperty(value = "month")
    @Range(min = 1, max = 12, message = "The forecasted month must be between 1 and 31")
    private int month;

    @JsonProperty(value = "min_temp")
    @Range(min = -50, message = "The minimist temp is 50")
    private int minTemp;

    @JsonProperty(value = "max_temp")
    @Range(max = 50, message = "The maximum temp is 50")
    private int maxTemp;

    @JsonProperty(value = "precipitation")
    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    private int precipitation;

    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    @NotBlank(message = "Status must not be empty")
    @JsonProperty(value = "status")
    private String status;
}
