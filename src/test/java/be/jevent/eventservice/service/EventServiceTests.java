package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.repository.LocationRepository;
import org.apache.commons.fileupload.FileUploadException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServiceTests {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private LocationRepository locationRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Autowired
    private EventService eventService;

    private Event event;
    private Location location;

    public void init() {
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
        event.setEventDate(LocalDate.now());
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
    public void getAllEventsTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAll()).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEvents();

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getBanner(), event.getBanner());
        assertEquals(eventDTOList.get(0).getThumbnail(), event.getThumbnail());
    }

    @Test
    public void getEventsByTypeTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAllByEventType(any(EventType.class))).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsByType(event.getEventType());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getEventName(), event.getEventName());
        assertEquals(eventDTOList.get(0).getShortDescription(), event.getShortDescription());
    }

    @Test
    public void getAllEventsByTypeAndCityTest() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAllByEventType_AndLocation_City(any(EventType.class), anyString())).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsByTypeAndCity(event.getEventType(), event.getLocation().getCity());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getEventDate(), event.getEventDate());
        assertEquals(eventDTOList.get(0).getPrice(), event.getPrice());
        assertEquals(eventDTOList.get(0).isAccepted(), event.isAccepted());
    }

//    @Test
//    public void createEventTest() throws IOException, FileUploadException {
//        init();
//        when(eventRepository.save(any(Event.class))).thenReturn(event);
//        when(locationRepository.findById(any())).thenReturn(Optional.ofNullable(location));
//
//        String banner = "banner.jpg";
//        String thumb = "thumb.jpg";
//        MockMultipartFile fileBanner = new MockMultipartFile("banner",banner,
//                "text/plain", "test data".getBytes());
//        MockMultipartFile fileThumb = new MockMultipartFile("thumb",thumb,
//                "text/plain", "test data".getBytes());
//
//        CreateEventResource eventResource =
//                new CreateEventResource(event.getEventName(), event.getEventType().getType(), event.getShortDescription(),
//                        event.getDescription(), event.getEventDate(), event.getEventTime(),
//                        location.getId().toString(), event.getPrice(), event.getTicketsLeft(),
//                        banner, thumb);
//
//        fileStorageService.save(fileBanner);
//        fileStorageService.save(fileThumb);
//
//        eventService.createEvent(eventResource, fileBanner, fileThumb, anyString());
//    }

//    @Test
//    public void getEventByIdtest(){
//        init();
//
//        when(eventRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(event));
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

        when(eventRepository.findAllByTicketOffice(anyString())).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsFromTicketOffice(event.getTicketOffice());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getTicketsLeft(), event.getTicketsLeft());
        assertEquals(eventDTOList.get(0).getId(), event.getId());
        assertEquals(eventDTOList.get(0).getEventType(), event.getEventType().getType());
    }

    @Test
    public void getAllEventsFromTicketOfficeAndType() {
        init();
        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAllByEventTypeAndTicketOffice(any(EventType.class), anyString())).thenReturn(eventList);

        List<EventDTO> eventDTOList = eventService.getAllEventsFromTicketOfficeAndType(event.getTicketOffice(), event.getEventType());

        assertEquals(eventDTOList.size(), eventList.size());
        assertEquals(eventDTOList.get(0).getLocation().getBuildingName(), event.getLocation().getBuildingName());
        assertEquals(eventDTOList.get(0).getEventTime(), event.getEventTime());
        assertEquals(eventDTOList.get(0).getDescription(), event.getDescription());
    }

    @Test
    public void throwExceptionEventByIdNotFound() {
        Long id = 2L;
        Throwable thrown = assertThrows(EventException.class, () -> eventService.getEventById(id));
        assertEquals("Event not found", thrown.getMessage());
    }

    @Test
    public void throwExceptionNoEventsFound() {
        Throwable thrown = assertThrows(EventException.class, () -> eventService.getAllEvents());
        assertEquals("No events found", thrown.getMessage());
    }

    @Test
    public void throwExceptionNoEventsFoundFromTicketOffice() {
        Throwable thrown = assertThrows(EventException.class, () -> eventService.getAllEventsFromTicketOffice(anyString()));
        assertEquals("No events found", thrown.getMessage());
    }

    @Test
    public void throwExceptionNoEventsFoundFromTicketOfficeAndType() {
        Throwable thrown = assertThrows(EventException.class, () -> eventService.getAllEventsFromTicketOfficeAndType(anyString(), any(EventType.class)));
        assertEquals("No events found", thrown.getMessage());
    }

}
