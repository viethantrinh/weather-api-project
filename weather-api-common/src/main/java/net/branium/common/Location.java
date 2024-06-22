package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "city_name", length = 128, nullable = false)
    private String cityName;

    @Column(name = "region_name", length = 128, nullable = true)
    private String regionName;

    @Column(name = "country_name", length = 64, nullable = false)
    private String countryName;

    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;

    @Column(name = "enabled", nullable = true)
    private boolean enabled;

    @Column(name = "trashed", nullable = true)
    private boolean trashed;


    public Location() {

    }

    public Location(String code, String cityName, String regionName, String countryName, String countryCode, boolean enabled, boolean trashed) {
        this.code = code;
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.enabled = enabled;
        this.trashed = trashed;
    }
}
