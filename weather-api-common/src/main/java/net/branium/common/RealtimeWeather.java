package net.branium.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public void setLocation(Location location) {
        this.locationCode = location.getCode();
        this.location = location;
    }
}
