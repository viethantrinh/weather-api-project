package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Embeddable
@EqualsAndHashCode
public class HourlyWeatherId implements Serializable {

    @Column(name = "hour_of_day")
    private int hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;

    public HourlyWeatherId() {

    }

    public HourlyWeatherId(int hourOfDay, Location location) {
        this.hourOfDay = hourOfDay;
        this.location = location;
    }
}
