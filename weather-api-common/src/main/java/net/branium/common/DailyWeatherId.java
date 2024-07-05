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
public class DailyWeatherId implements Serializable {

    @Column(name = "day_of_month")
    private int dayOfMonth;

    @Column(name = "month")
    private int month;

    @ManyToOne
    @JoinColumn(name = "location_code")
    private Location location;
}
