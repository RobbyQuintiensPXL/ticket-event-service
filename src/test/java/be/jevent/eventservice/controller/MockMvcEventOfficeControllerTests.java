package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(EventOfficeController.class)
public class MockMvcEventOfficeControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EventService eventService;

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
        location = new Location();
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
    public void getAllEventsTest() throws Exception {
        init();
        List<EventDTO> eventDTOS = new LinkedList<>();
        eventDTOS.add(new EventDTO(event));
        Page<EventDTO> events = new PageImpl<>(eventDTOS);
        when(eventService.getAllEventsFromTicketOffice(any(Predicate.class), anyString(), anyInt(), anyInt()))
                .thenReturn(events);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/office/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    public void createEventTest() throws Exception {
//        init();
//        LocalDate localDate = LocalDate.now().plusDays(2);
//        LocalTime localTime = LocalTime.now();
//        mockMvc
//                .perform(MockMvcRequestBuilders.post("/office/event/post")
//                .content(asJsonString(new CreateEventResource("EventName", EventType.CONCERT.getType(),
//                "ShortDesc", "Desc", localDate, localTime,
//                "Location", 50.0, 100, "thumb", "banner")))
//                                        .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//    }
}
