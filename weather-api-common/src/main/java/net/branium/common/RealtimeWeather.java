package net.branium.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@Entity
@Table(name = "weather_realtime")
public class RealtimeWeather {

    @JsonIgnore
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "location_code")
    private String locationCode;

    @JsonProperty(value = "temperature")
    @Range(min = -50, max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
    @Column(name = "temperature")
    private int temperature;

    @JsonProperty(value = "humidity")
    @Range(min = 0, max = 100, message = "Humidity must be in the range of 0 to 100 percentage")
    @Column(name = "humidity")
    private int humidity;

    @Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
    @JsonProperty(value = "precipitation")
    @Column(name = "precipitation")
    private int precipitation;

    @Range(min = 0, max = 200, message = "Wind speed must be in the range of 0 to 200 km/h")
    @JsonProperty(value = "wind_speed")
    @Column(name = "wind_speed")
    private int windSpeed;

    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    @NotBlank(message = "Status must not be empty")
    @JsonProperty(value = "status")
    @Column(name = "status", length = 50)
    private String status;

    @JsonIgnore
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;

    public RealtimeWeather() {
    }

    public RealtimeWeather(String locationCode, int temperature, int humidity, int precipitation,
                           int windSpeed, String status, LocalDateTime lastUpdated, Location location) {
        this.locationCode = locationCode;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.windSpeed = windSpeed;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.location = location;
    }

    public void setLocation(Location location) {
        this.locationCode = location.getCode();
        this.location = location;
    }
}
