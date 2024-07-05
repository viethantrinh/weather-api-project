package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class HourlyWeatherId implements Serializable {

    @Column(name = "hour_of_day")
    private int hourOfDay;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;
}
