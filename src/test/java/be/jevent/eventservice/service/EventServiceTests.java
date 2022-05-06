package be.jevent.eventservice.service;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import be.jevent.eventservice.repository.EventRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTests {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    private Event event;
    private Location location;
    private TicketOffice ticketOffice;

    public void init(){
        ticketOffice = new TicketOffice();
        ticketOffice.setOrganisation("Organisation");
        event = new Event();
        location = new Location();
        location.setBuildingName("Building");
        location.setTicketOffice(ticketOffice);
        location.setCity("City");
        event.setEventName("Test");
        event.setEventType(EventType.CULTURE);
        event.setId(1L);
        event.setDescription("Description");
        event.setShortDescription("Short Description");
        event.setEventDate(LocalDate.now());
        event.setEventTime(LocalTime.now());
        event.setLocation(location);
    }

    @Test
    public void getAllEventsTest(){
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAll()).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEvents();

        assertEquals(eventDTOList.size(), eventList.size());
    }

    @Test
    public void getEventsByTypeTest(){
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAllByEventType(any(EventType.class))).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsByType(event.getEventType());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getEventName(), event.getEventName());
    }

    @Test
    public void getAllEventsByTypeAndCityTest(){
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAllByEventType_AndLocation_City(any(EventType.class), anyString())).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsByTypeAndCity(event.getEventType(), event.getLocation().getCity());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getEventDate(), event.getEventDate());
    }

    @Test
    public void getEventByIdtest(){
        init();

        when(eventRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(event));

        EventDTO eventDTO = eventService.getEventById(event.getId());

        assertEquals(eventDTO.getDescription(), event.getDescription());
        assertEquals(eventDTO.getShortDescription(), event.getShortDescription());
    }

    @Test
    public void throwExceptionEventByIdNotFound(){
        Long id = 2L;
        Throwable thrown = assertThrows(EventException.class, () -> eventService.getEventById(id));
        assertEquals("Event not found", thrown.getMessage());
    }

}
