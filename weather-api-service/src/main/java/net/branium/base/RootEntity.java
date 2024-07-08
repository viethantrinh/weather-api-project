package net.branium.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"locationsUrl", "locationByCodeUrl", "realtimeWeatherByIPUrl", "realtimeWeatherByCodeUrl",
        "hourlyForecastByIPUrl", "hourlyForecastByCodeUrl", "dailyForecastByIPUrl", "dailyForecastByCodeUrl",
        "fullWeatherByIPUrl", "fullWeatherByCodeUrl"})
public class RootEntity {
    @JsonProperty("location_url")
    private String locationsUrl;

    @JsonProperty("location_by_code_url")
    private String locationByCodeUrl;

    @JsonProperty("realtime_by_ip_url")
    private String realtimeWeatherByIPUrl;

    @JsonProperty("realtime_by_code_url")
    private String realtimeWeatherByCodeUrl;

    @JsonProperty("hourly_forecast_by_ip_url")
    private String hourlyForecastByIPUrl;

    @JsonProperty("hourly_forecast_by_code_url")
    private String hourlyForecastByCodeUrl;

    @JsonProperty("daily_forecast_by_ip_url")
    private String dailyForecastByIPUrl;

    @JsonProperty("daily_forecast_by_code_url")
    private String dailyForecastByCodeUrl;

    @JsonProperty("full_weather_by_ip_url")
    private String fullWeatherByIPUrl;

    @JsonProperty("full_weather_by_code_url")
    private String fullWeatherByCodeUrl;
}
