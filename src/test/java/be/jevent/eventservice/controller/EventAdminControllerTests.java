package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class EventAdminControllerTests {

    Predicate predicate;
    BooleanBuilder builder;

    @InjectMocks
    EventAdminController eventAdminController;

    @Mock
    EventService eventService;
    @Mock
    EventTypeService eventTypeService;
    Pageable pageable;

    private List<Event> eventList;
    private Event event;
    private Event event2;

    public void init() {
        builder = new BooleanBuilder();

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
        event2.setLocation(location2);
        event2.setTicketOffice("Organisation2");
        event2.setBanner("banner2");
        event2.setThumbnail("thumb2");

        eventList = new ArrayList<>();
        eventList.add(event);
        eventList.add(event2);

        pageable = PageRequest.of(0, 5);
    }

    @Test
    public void getAllEventsTest() {
        init();
        Page<EventDTO> events = new PageImpl<>(eventList).map(EventDTO::new);
        when(eventService.getNewEventsForAdmin(predicate, pageable.getPageNumber(), pageable.getPageSize())).thenReturn(events);

        ResponseEntity<Page<EventDTO>> responseEntity = eventAdminController.getAllEvents(pageable.getPageNumber(), pageable.getPageSize(), predicate);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void approveEventTest() {
        init();
        ResponseEntity<Void> responseEntity = eventAdminController.approveEvent(event.getId());

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void deleteEventTest() {
        init();

        when(eventService.deleteEvent(anyLong())).thenReturn(anyString());
        ResponseEntity<String> responseEntity = eventAdminController.deleteEvent(event.getId());

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }
}
