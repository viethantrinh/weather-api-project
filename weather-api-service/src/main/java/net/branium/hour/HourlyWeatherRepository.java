package net.branium.hour;

import net.branium.common.HourlyWeather;
import net.branium.common.HourlyWeatherId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {
}
