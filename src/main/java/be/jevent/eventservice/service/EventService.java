package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.service.client.TicketFeignClient;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private static final String TOPIC = "test";

    private final TicketFeignClient ticketFeignClient;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final MessageSource messageSource;
    private final FileStorageService storageService;
    private final TicketOfficeService ticketOfficeService;

    public EventService(TicketFeignClient ticketFeignClient, EventRepository eventRepository,
                        LocationService locationService, MessageSource messageSource,
                        FileStorageService storageService, TicketOfficeService ticketOfficeService) {
        this.ticketFeignClient = ticketFeignClient;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.messageSource = messageSource;
        this.storageService = storageService;
        this.ticketOfficeService = ticketOfficeService;
    }

    public List<EventDTO> getAllEvents() {
        List<EventDTO> eventDTOList = eventRepository.findAll().stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsFromTicketOffice(String username) {
        List<EventDTO> eventDTOList = eventRepository.findAllByTicketOffice_Email(username).stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsFromTicketOfficeAndType(String username, EventType type){
        List<EventDTO> eventDTOList = eventRepository.findAllByEventTypeAndTicketOffice_Email(type, username).stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsByType(EventType type) {
        List<EventDTO> eventDTOList = eventRepository.findAllByEventType(type).stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found for " + type.getType());
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsByTypeAndCity(EventType type, String city) {
        List<EventDTO> eventDTOList = eventRepository.findAllByEventType_AndLocation_City(type, city)
                .stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found for " + type.getType() + " in " + city);
        }
        return eventDTOList;
    }

    public EventDTO getEventById(Long id) {
        Optional<EventDTO> eventDTO = eventRepository.findById(id).map(EventDTO::new);
        if (eventDTO.isEmpty()) {
            throw new EventException("Event not found");
        }
        eventDTO.get().setTicketsLeft(eventDTO.get().getTicketsLeft() - retrieveTicketsSold(id));
        return eventDTO.get();
    }

    public void createEvent(CreateEventResource eventResource,
                            MultipartFile banner, MultipartFile thumb, String user) throws IOException, FileUploadException {
        if (EventType.forName(eventResource.getEventType()) == null) {
            throw new EventException("Event type " + eventResource.getEventType() + " not found");
        }

        Location location = locationService.getLocationById(Long.parseLong(eventResource.getLocation()));

        TicketOffice ticketOffice = ticketOfficeService.getTicketOfficeByUsername(user);

        Event event = new Event();
        event.setEventName(eventResource.getEventName());
        event.setEventType(EventType.valueOf(eventResource.getEventType()));
        event.setShortDescription(eventResource.getShortDescription());
        event.setDescription(eventResource.getDescription());
        event.setEventDate(eventResource.getEventDate());
        event.setEventTime(eventResource.getEventTime());
        event.setLocation(location);
        event.setTicketsLeft(eventResource.getTicketsLeft());
        event.setPrice(eventResource.getPrice());
        event.setBanner(banner.getOriginalFilename());
        event.setThumbnail(thumb.getOriginalFilename());
        event.setTicketOffice(ticketOffice);

        storageService.save(thumb);
        storageService.save(banner);

        eventRepository.save(event);
        String timeStamp = new SimpleDateFormat().format( new Date() );
        LOGGER.info("event created: " + event.getEventName() + " on " + timeStamp.toString());
    }

    public String deleteEvent(Long id) {
        eventRepository.deleteById(id);

        return "event deleted";
    }

    public void updateEvent(Event event) {
        eventRepository.save(event);
    }

    public int retrieveTicketsSold(Long eventId) {
        return ticketFeignClient.getTicketsSold(eventId);
    }

}
