package net.branium.common;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@Entity
@Table(name = "weather_realtime")
public class RealtimeWeather {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "humidity")
    private int humidity;

    @Column(name = "precipitation")
    private int precipitation;

    @Column(name = "wind_speed")
    private int windSpeed;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

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
