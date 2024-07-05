package net.branium.daily;

import net.branium.realtime.RealtimeWeatherApiController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RealtimeWeatherApiController.class)
@ComponentScan(
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DailyWeatherMapperImpl.class}),
        useDefaultFilters = false
)
class DailyWeatherApiControllerTests {

}