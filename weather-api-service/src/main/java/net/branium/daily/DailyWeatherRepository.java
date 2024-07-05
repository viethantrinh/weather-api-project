package net.branium.daily;

import net.branium.common.DailyWeather;
import net.branium.common.DailyWeatherId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {
}
