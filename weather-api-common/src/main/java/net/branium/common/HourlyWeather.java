package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "weather_hourly")
public class HourlyWeather {
    @EqualsAndHashCode.Include
    @EmbeddedId
    private HourlyWeatherId id;

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "precipitation")
    private int precipitation;

    @Column(length = 50)
    private String status;
}
