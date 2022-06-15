package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import com.querydsl.core.types.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(EventController.class)
public class MockMvcEventControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventTypeService eventTypeService;

    @Autowired
    private MockMvc mockMvc;

    private Event event;

    public void init() {
        Location location = new Location();
        location.setId(1L);
        location.setCity("City");
        location.setBuildingName("Building");
        event = new Event();
        event.setEventName("Test");
        event.setEventType(EventType.CULTURE);
        event.setId(1L);
        event.setDescription("Description");
        event.setShortDescription("Short Description");
        event.setEventDate(LocalDate.now().plusDays(2));
        event.setEventTime(LocalTime.now());
        event.setTicketsLeft(200);
        event.setPrice(100);
        event.setAccepted(true);
        event.setTicketOffice("Organisation");
        event.setBanner("banner");
        event.setThumbnail("thumb");
        event.setLocation(location);
    }

    @Test
    public void getAllEventTypesTest() throws Exception {
        init();
        when(eventTypeService.getAllEventTypes())
                .thenReturn(Stream.of(EventType.values()).map(EventType::getType).collect(Collectors.toList()));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/events/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllEventsTest() throws Exception {
        init();
        List<EventDTO> eventDTOS = new LinkedList<>();
        eventDTOS.add(new EventDTO(event));
        Page<EventDTO> events = new PageImpl<>(eventDTOS);
        when(eventService.getAllEventsImpl(anyInt(), anyInt()))
                .thenReturn(events);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getEventByIdTest() throws Exception {
        init();
        when(eventService.getEventById(anyLong()))
                .thenReturn(new EventDTO(event));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/events/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.eventName").value(event.getEventName()));
    }

    @Test
    public void getEventsByTypeAndCityTest() throws Exception {
        init();
        List<EventDTO> eventDTOS = new LinkedList<>();
        eventDTOS.add(new EventDTO(event));
        Page<EventDTO> events = new PageImpl<>(eventDTOS);
        when(eventService.findByTypeCity(any(Predicate.class), anyInt(), anyInt()))
                .thenReturn(events);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/events/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].eventType")
                        .value(event.getEventType().getType()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].location.buildingName")
                        .value(event.getLocation().getBuildingName()));
    }
}
