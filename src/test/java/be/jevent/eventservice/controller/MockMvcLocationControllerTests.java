package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(LocationController.class)
public class MockMvcLocationControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private LocationService locationService;

    @Autowired
    private MockMvc mockMvc;

    private Location location;
    private Event event;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() {
        event = new Event();
        event.setId(1L);
        event.setEventName("Event");

        location = new Location();
        location.setId(1L);
        location.setCity("City");
        location.setBuildingName("Building");
        event.setLocation(location);
    }

    @Test
    public void getAllLocationsTest() throws Exception {
        init();
        List<LocationDTO> locationDTOS = new LinkedList<>();
        locationDTOS.add(new LocationDTO(location));
        when(locationService.getAllLocations())
                .thenReturn(locationDTOS);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/alllocations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllCitiesTest() throws Exception {
        init();
        List<Event> events = new LinkedList<>();
        events.add(event);
        List<String> cities = events.stream().map(Event::getLocation).map(Location::getCity).distinct().collect(Collectors.toList());
        when(locationService.getAllLocationCities())
                .thenReturn(cities);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/locations/city")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(location.getCity()));
    }

    @Test
    public void createLocationTest() throws Exception {
        init();
        List<LocationDTO> locationDTOS = new LinkedList<>();
        locationDTOS.add(new LocationDTO(location));

        mockMvc
                .perform(MockMvcRequestBuilders.post("/office/add_location")
                        .content(asJsonString(new CreateLocationResource("Building", 3500,
                                "City", "Address", "Country")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getLocationByIdTest() throws Exception {
        init();
        when(locationService.getLocationById(anyLong()))
                .thenReturn(location);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/locations/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(location.getCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buildingName").value(location.getBuildingName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(location.getId()));
    }

    @Test
    public void getLocationsByTicketOfficeTest() throws Exception {
        init();
        List<LocationDTO> locationDTOS = new LinkedList<>();
        locationDTOS.add(new LocationDTO(location));

        when(locationService.getLocationsByTicketOffice(anyString()))
                .thenReturn(locationDTOS);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/office/locations")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
