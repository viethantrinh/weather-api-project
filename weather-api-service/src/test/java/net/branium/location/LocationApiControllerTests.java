package net.branium.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.branium.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.annotation.Documented;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationApiController.class)
class LocationApiControllerTests {

    private static final String END_POINT_PATH = "/v1/locations";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocationService locationService;

    @Test
    void givenInvalidFieldLocation_whenSendRequest_thenShouldReturn404BadRequest() throws Exception {
        Location location = new Location();
        String jsonBody = objectMapper.writeValueAsString(location);
        System.out.println(jsonBody);
        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void givenValidFieldLocation_whenCreated_thenShouldReturn201Created() throws Exception {
        Location location = Location.builder()
                .code("NYC_USA")
                .cityName("New York City")
                .regionName("New York")
                .countryCode("US")
                .countryName("United State of America")
                .enabled(true)
                .build();

        when(locationService.createLocation(any(Location.class))).thenReturn(location);

        String locationJson = objectMapper.writeValueAsString(location);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON).content(locationJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", is("NYC_USA")))
                .andExpect(header().string("Location", "/v1/locations/NYC_USA"))
                .andDo(print());
    }
}