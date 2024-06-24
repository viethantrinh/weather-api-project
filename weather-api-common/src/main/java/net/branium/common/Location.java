package net.branium.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @NotNull(message = "Location code must not be null")
    @Length(min = 2, max = 10, message = "Location code must be between 2 and 10 characters")
    @EqualsAndHashCode.Include
    @Id
    @JsonProperty(value = "code")
    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @NotNull(message = "City name must not be null")
    @Length(min = 3, max = 128, message = "City name must be between 3 and 128 characters")
    @JsonProperty(value = "city_name")
    @Column(name = "city_name", length = 128, nullable = false)
    private String cityName;

    @Length(min = 3, max = 128, message = "Region name must be between 3 and 128 characters")
    @JsonProperty(value = "region_name")
    @Column(name = "region_name", length = 128, nullable = true)
    private String regionName;

    @NotNull(message = "Country name must not be null")
    @Length(min = 3, max = 64, message = "Country name must be between 3 and 64 characters")
    @JsonProperty(value = "country_name")
    @Column(name = "country_name", length = 64, nullable = false)
    private String countryName;

    @NotNull(message = "Country code must not be null")
    @Length(min = 1, max = 2, message = "Country code must be between 1 and 2 characters")
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
