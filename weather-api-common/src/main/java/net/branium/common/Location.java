package net.branium.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
    @JsonProperty(value = "code")
    @Column(name = "code", length = 10, nullable = false, unique = true)
    @NotBlank
    private String code;

    @NotBlank
    @JsonProperty(value = "city_name")
    @Column(name = "city_name", length = 128, nullable = false)
    private String cityName;

    @JsonProperty(value = "region_name")
    @Column(name = "region_name", length = 128, nullable = true)
    private String regionName;

    @NotBlank
    @JsonProperty(value = "country_name")
    @Column(name = "country_name", length = 64, nullable = false)
    private String countryName;

    @NotBlank
    @JsonProperty(value = "country_code")
    @Column(name = "country_code", length = 2, nullable = false)
    private String countryCode;

    @JsonProperty(value = "enabled")
    @Column(name = "enabled", nullable = true)
    private boolean enabled;

    @JsonIgnore
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
