package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.service.client.TicketFeignClient;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    // private static final String TOPIC = "test";

    private final TicketFeignClient ticketFeignClient;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final MessageSource messageSource;
    private final FileStorageService storageService;

    public EventService(TicketFeignClient ticketFeignClient, EventRepository eventRepository,
                        LocationService locationService, MessageSource messageSource,
                        FileStorageService storageService) {
        this.ticketFeignClient = ticketFeignClient;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.messageSource = messageSource;
        this.storageService = storageService;
    }

    public List<EventDTO> getAllEvents() {
        List<EventDTO> eventDTOList = eventRepository.findAll().stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsFromTicketOffice(String ticketOffice) {
        List<EventDTO> eventDTOList = eventRepository.findAllByTicketOffice(ticketOffice).stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return eventDTOList;
    }

    public List<EventDTO> getAllEventsFromTicketOfficeAndType(String ticketOffice, EventType type){
        List<EventDTO> eventDTOList = eventRepository.findAllByEventTypeAndTicketOffice(type, ticketOffice).stream().map(EventDTO::new).collect(Collectors.toList());
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
                            MultipartFile banner, MultipartFile thumb, String ticketOffice) throws IOException, FileUploadException {
        if (EventType.forName(eventResource.getEventType()) == null) {
            throw new EventException("Event type " + eventResource.getEventType() + " not found");
        }

        Location location = locationService.getLocationById(Long.parseLong(eventResource.getLocation()));

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
