package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "weather_daily")
@Entity
public class DailyWeather {
    @EqualsAndHashCode.Include
    @EmbeddedId
    private DailyWeatherId id;

    @Column(name = "min_temp")
    private int minTemp;

    @Column(name = "max_temp")
    private int maxTemp;

    @Column(name = "precipitation")
    private int precipitation;

    @Column(name = "status", length = 50)
    private String status;
}
