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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "weather_hourly")
public class HourlyWeather {
    @EqualsAndHashCode.Include
    @EmbeddedId
    private HourlyWeatherId id = new HourlyWeatherId();

    @Column(name = "temperature")
    private int temperature;

    @Column(name = "precipitation")
    private int precipitation;

    @Column(length = 50)
    private String status;

    public HourlyWeather() {

    }

    public HourlyWeather(HourlyWeatherId id, int temperature, int precipitation, String status) {
        this.id = id;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.status = status;
    }

    @Override
    public String toString() {
        return "HourlyWeather{" +
                "id=" + id.getHourOfDay() + " " + id.getLocation() +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
