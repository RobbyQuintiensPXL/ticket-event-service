package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventPageRepository;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.repository.LocationRepository;
import be.jevent.eventservice.service.client.TicketFeingClientMock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTests {

    Predicate predicate;
    BooleanBuilder builder;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private EventPageRepository eventPageRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private TicketFeingClientMock ticketFeignClient;
    @Autowired
    private EventService eventService;
    private Event event;
    private Location location;

    public void init() {
        builder = new BooleanBuilder();

        event = new Event();
        location = new Location();
        location.setId(1L);
        location.setBuildingName("Building");
        location.setCity("City");
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
        event.setLocation(location);
        event.setTicketOffice("Organisation");
        event.setBanner("banner");
        event.setThumbnail("thumb");
    }

    @Test
    public void findByTypeCityTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);
        Page<Event> events = new PageImpl<>(eventList);
        Pageable paging = PageRequest.of(0, 5);

        when(eventPageRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(events);

        Page<EventDTO> eventDTOList = eventService.findByTypeCity(predicate, paging.getPageNumber(), paging.getPageSize());

        List<EventDTO> eventDTOS = eventDTOList.get().collect(Collectors.toList());

        assertEquals(eventDTOS.size(), eventList.size());
        assertEquals(eventDTOS.get(0).getBanner(), event.getBanner());
        assertEquals(eventDTOS.get(0).getThumbnail(), event.getThumbnail());
    }

    @Test
    public void getAllEventsImplTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);
        when(eventRepository.findAll()).thenReturn(eventList);
        Page<EventDTO> events = new PageImpl<>(eventList.stream().map(EventDTO::new).collect(Collectors.toList()));
        List<EventDTO> eventDTOS = events.get().collect(Collectors.toList());

        assertEquals(eventDTOS.get(0).getDescription(), eventList.get(0).getDescription());
        assertEquals(eventDTOS.get(0).getEventName(), eventList.get(0).getEventName());
    }

//    @Test
//    public void createEventTest() throws IOException, FileUploadException {
//        init();
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//        when(locationRepository.findById(any())).thenReturn(Optional.ofNullable(location));
//
//        String banner = "banner.jpg";
//        String thumb = "thumb.jpg";
//        MockMultipartFile fileBanner = new MockMultipartFile("banner", banner,
//                "text/plain", "test data".getBytes());
//        MockMultipartFile fileThumb = new MockMultipartFile("thumb", thumb,
//                "text/plain", "test data".getBytes());
//
//        CreateEventResource eventResource =
//                new CreateEventResource(event.getEventName(), event.getEventType().getType(), event.getShortDescription(),
//                        event.getDescription(), event.getEventDate(), event.getEventTime(),
//                        location.getId().toString(), event.getPrice(), event.getTicketsLeft(),
//                        banner, thumb);
//
//        eventService.createEvent(eventResource, fileBanner, fileThumb, anyString());
//
//        assertEquals(eventResource.getEventDate(), event.getEventDate());
//        assertEquals(eventResource.getEventTime(), event.getEventTime());
//        assertEquals(eventResource.getDescription(), event.getDescription());
//        assertEquals(eventResource.getShortDescription(), event.getShortDescription());
//        assertEquals(eventResource.getPrice(), event.getPrice());
//    }

//    @Test
//    public void getEventByIdtest() {
//        init();
//        int tickets = 2;
//        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
//        when(ticketFeignClient.getTicketsSold(anyLong())).thenReturn(tickets);
//
//        EventDTO eventDTO = eventService.getEventById(event.getId());
//
//        assertEquals(eventDTO.getDescription(), event.getDescription());
//        assertEquals(eventDTO.getShortDescription(), event.getShortDescription());
//    }

    @Test
    public void getAllEventsFromTicketOfficeTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);
        Page<Event> events = new PageImpl<>(eventList);
        Pageable paging = PageRequest.of(0, 5);

        when(eventPageRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(events);
        Page<EventDTO> eventDTOList = eventService.getAllEventsFromTicketOffice(predicate, event.getTicketOffice(), paging.getPageNumber(), paging.getPageSize());
        List<EventDTO> eventDTOS = eventDTOList.get().collect(Collectors.toList());

        assertEquals(eventDTOS.size(), eventList.size());
        assertEquals(eventDTOS.get(0).getTicketsLeft(), event.getTicketsLeft());
        assertEquals(eventDTOS.get(0).getId(), event.getId());
        assertEquals(eventDTOS.get(0).getEventType(), event.getEventType().getType());
    }

    @Test
    public void getNewEventsForAdminTest() {
        init();
        event.setAccepted(false);
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);
        Page<Event> events = new PageImpl<>(eventList);
        Pageable paging = PageRequest.of(0, 5);

        when(eventPageRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(events);
        Page<EventDTO> eventDTOList = eventService.getNewEventsForAdmin(predicate, paging.getPageNumber(), paging.getPageSize());
        List<EventDTO> eventDTOS = eventDTOList.get().collect(Collectors.toList());

        assertEquals(eventDTOS.size(), eventList.size());
        assertEquals(eventDTOS.get(0).getTicketsLeft(), event.getTicketsLeft());
        assertEquals(eventDTOS.get(0).getId(), event.getId());
        assertEquals(eventDTOS.get(0).getEventType(), event.getEventType().getType());
        assertFalse(eventDTOS.get(0).isAccepted());
    }

    @Test
    public void approveEventTest() {
        init();
        event.setAccepted(false);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        eventService.approveEvent(event.getId());

        assertTrue(event.isAccepted());
    }

    @Test
    public void deleteEventTest() {
        init();
        eventService.deleteEvent(event.getId());
        verify(eventRepository).deleteById(event.getId());
    }

    @Test
    public void retrieveTicketsSoldTest() {
        init();
        int ticket = 2;
        when(ticketFeignClient.getTicketsSold(anyLong())).thenReturn(ticket);

    }

    @Test(expected = EventException.class)
    public void throwExceptionEventByIdNotFound() {
        eventService.getEventById(anyLong());
    }


    @Test(expected = EventException.class)
    public void throwExceptionNoEventsFound() {
        eventService.getAllEventsImpl(1, 1);
    }

    @Test
    public void throwExceptionNoEventsByTypeCityFound() {
        eventService = mock(EventService.class);
        when(eventService.findByTypeCity(any(Predicate.class), anyInt(), anyInt())).thenReturn(null);
    }

    @Test
    public void throwExceptionNoEventsFromOfficeFound() {
        eventService = mock(EventService.class);
        when(eventService.getAllEventsFromTicketOffice(any(Predicate.class), anyString(), anyInt(), anyInt())).thenReturn(null);
    }
}
