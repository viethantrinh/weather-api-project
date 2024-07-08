package net.branium.realtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@JsonPropertyOrder({"location", "temperature", "humidity", "precipitation", "status", "windSpeed", "lastUpdated"})
public class RealtimeWeatherDTO extends RepresentationModel<RealtimeWeatherDTO> {
    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "temperature")
    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    private int temperature;

    @JsonProperty(value = "humidity")
    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    private int humidity;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    @JsonProperty(value = "precipitation")
    private int precipitation;

    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    @NotBlank(message = "Status must not be empty")
    @JsonProperty(value = "status")
    private String status;

    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
    @JsonProperty(value = "wind_speed")
    private int windSpeed;

    @JsonProperty(value = "last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdated;
}
