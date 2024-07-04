package net.branium.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"code", "cityName", "regionName", "countryCode", "countryName", "enabled"})
public class LocationDTO {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "city_name")
    private String cityName;

    @JsonProperty(value = "region_name")
    private String regionName;

    @JsonProperty(value = "country_name")
    private String countryName;

    @JsonProperty(value = "country_code")
    private String countryCode;

    @JsonProperty(value = "enabled")
    private boolean enabled;
}
