package net.branium.hourly;

import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {

    @Query("""
        SELECT h FROM HourlyWeather h
        WHERE
            h.id.location.code = ?1
            AND
            h.id.hourOfDay > ?2
            AND
            h.id.location.trashed = false""")
    List<HourlyWeather> findByLocationCodeAndCurrentHour(String locationCode, int currentHour);
}
