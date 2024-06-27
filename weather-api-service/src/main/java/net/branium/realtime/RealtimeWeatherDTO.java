package net.branium.realtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RealtimeWeatherDTO {
    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "temperature")
    private int temperature;

    @JsonProperty(value = "humidity")
    private int humidity;

    @JsonProperty(value = "precipitation")
    private int precipitation;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "wind_speed")
    private int windSpeed;

    @JsonProperty(value = "last_updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdated;
}
