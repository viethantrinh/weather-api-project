package net.branium.realtime;

import net.branium.common.RealtimeWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

    @Query(value = "SELECT r FROM RealtimeWeather r WHERE r.location.countryCode = :countryCode " +
            "AND r.location.cityName = :cityName AND r.location.trashed = false")
    Optional<RealtimeWeather> findByCountryCodeAndCity(@Param("countryCode") String countryCode, @Param("cityName") String cityName);

    @Query(value = "SELECT r FROM RealtimeWeather r WHERE r.location.code = ?1 AND r.location.trashed = false")
    Optional<RealtimeWeather> findByLocationCode(String locationCode);
}
