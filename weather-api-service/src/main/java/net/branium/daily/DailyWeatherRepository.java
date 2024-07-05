package net.branium.daily;

import net.branium.common.DailyWeather;
import net.branium.common.DailyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("""
        SELECT d FROM DailyWeather d
        WHERE
            d.id.location.code = ?1
            AND
            d.id.location.trashed = false
        ORDER BY
            d.id.month,
            d.id.dayOfMonth
        """)
    List<DailyWeather> findByLocationCode(String locationCode);



}
