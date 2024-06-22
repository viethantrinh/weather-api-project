package net.branium.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
