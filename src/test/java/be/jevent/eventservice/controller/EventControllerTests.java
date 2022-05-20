package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class EventControllerTests {

    @InjectMocks
    EventController eventController;

    @Mock
    EventService eventService;

    @Mock
    EventTypeService eventTypeService;

    private List<Event> eventList;
    private Event event;
    private Event event2;

    public void init() {
        Location location = new Location();
        location.setId(1L);
        location.setBuildingName("Building");
        location.setCity("City");

        Location location2 = new Location();
        location2.setId(2L);
        location2.setBuildingName("Building2");
        location2.setCity("City2");

        event = new Event();
        event.setEventName("Test");
        event.setEventType(EventType.CULTURE);
        event.setId(1L);
        event.setDescription("Description");
        event.setShortDescription("Short Description");
        event.setEventDate(LocalDate.now());
        event.setEventTime(LocalTime.now());
        event.setTicketsLeft(200);
        event.setPrice(100);
        event.setAccepted(true);
        event.setLocation(location);
        event.setTicketOffice("Organisation");
        event.setBanner("banner");
        event.setThumbnail("thumb");

        event2 = new Event();
        event2.setEventName("Test2");
        event2.setEventType(EventType.CONCERT);
        event2.setId(2L);
        event2.setDescription("Description2");
        event2.setShortDescription("Short Description2");
        event2.setEventDate(LocalDate.now());
        event2.setEventTime(LocalTime.now());
        event2.setTicketsLeft(100);
        event2.setPrice(300);
        event2.setAccepted(true);
        event2.setLocation(location2);
        event2.setTicketOffice("Organisation2");
        event2.setBanner("banner2");
        event2.setThumbnail("thumb2");

        eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(event2);
    }

    @Test
    public void getAllEventsTest()
    {
        init();
        when(eventService.getAllEvents()).thenReturn(eventList.stream().map(EventDTO::new).collect(Collectors.toList()));

        ResponseEntity<List<EventDTO>> responseEntity = eventController.getAllEvents();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getEventById()
    {
        init();
        EventDTO eventDTO = Optional.of(event).stream().map(EventDTO::new).findAny().get();
        when(eventService.getEventById(anyLong())).thenReturn(eventDTO);

        ResponseEntity<EventDTO> responseEntity = eventController.getEventById(event.getId());

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getDescription()).isEqualTo(event.getDescription());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getEventName()).isEqualTo(eventDTO.getEventName());
    }

    /*@Test
    public void getEventsByTypeAndCity(){
        init();
        when(eventService.getAllEventsByType(any(EventType.class))).thenReturn(eventList.stream().map(EventDTO::new).collect(Collectors.toList()));

        ResponseEntity<List<EventDTO>> responseEntity = eventController.getEventsByTypeAndCity();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);

    }*/


//    MockHttpServletRequest request = new MockHttpServletRequest();
//    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

}
