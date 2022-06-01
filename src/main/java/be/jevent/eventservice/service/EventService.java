package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.filter.EventPredicates;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventPageRepository;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.service.client.TicketFeignClient;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final TicketFeignClient ticketFeignClient;
    private final EventRepository eventRepository;
    private final EventPageRepository eventPageRepository;
    private final LocationService locationService;
    private final FileStorageService storageService;

    public EventService(TicketFeignClient ticketFeignClient, EventRepository eventRepository,
                        LocationService locationService,
                        FileStorageService storageService, EventPageRepository eventPageRepository) {
        this.ticketFeignClient = ticketFeignClient;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.storageService = storageService;
        this.eventPageRepository = eventPageRepository;
    }

    public Page<EventDTO> getAllEventsImpl(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<EventDTO> list = eventRepository.findAll().stream().map(EventDTO::new).filter(EventDTO::isAccepted).collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new EventException("No events found");
        }
        return new PageImpl<>(list, paging, list.size());
    }

    public Page<EventDTO> findByTypeCity(Predicate predicate, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        BooleanBuilder builder = new BooleanBuilder();
        List<EventDTO> eventDTOList = eventPageRepository.findAll(builder.and(predicate), paging).stream().map(EventDTO::new).filter(EventDTO::isAccepted).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return new PageImpl<>(eventDTOList, paging, eventDTOList.size());
    }

    public Page<EventDTO> getAllEventsFromTicketOffice(Predicate predicate, String ticketOffice, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        BooleanBuilder builder = new BooleanBuilder();
        Predicate predicate1 = EventPredicates.eventsByTicketOffice(ticketOffice);

        List<EventDTO> eventDTOList = eventPageRepository.findAll(builder.and(predicate).and(predicate1), paging).stream().map(EventDTO::new).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return new PageImpl<>(eventDTOList, paging, eventDTOList.size());
    }

    public Page<EventDTO> getNewEventsForAdmin(Predicate predicate, int page, int size){
        Pageable paging = PageRequest.of(page, size);
        BooleanBuilder builder = new BooleanBuilder();

        List<EventDTO> eventDTOList = eventPageRepository.findAll(builder.and(predicate), paging).stream().map(EventDTO::new).filter(e -> !e.isAccepted()).collect(Collectors.toList());
        if (eventDTOList.isEmpty()) {
            throw new EventException("No events found");
        }
        return new PageImpl<>(eventDTOList, paging, eventDTOList.size());
    }

    public EventDTO getEventById(Long id) {
        Optional<EventDTO> eventDTO = eventRepository.findById(id).map(EventDTO::new);
        if (eventDTO.isEmpty()) {
            throw new EventException("Event not found");
        }
        eventDTO.get().setTicketsLeft(eventDTO.get().getTicketsLeft() - retrieveTicketsSold(id));
        return eventDTO.get();
    }

    public void approveEvent(Long id){
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new EventException("Event not found");
        }
        event.get().setAccepted(true);
        eventRepository.save(event.get());
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

    @Transactional
    public String deleteEvent(Long id, String ticketOffice) {
        eventRepository.deleteByIdAndTicketOffice(id, ticketOffice);
        return "event deleted";
    }

    public int retrieveTicketsSold(Long eventId) {
        return ticketFeignClient.getTicketsSold(eventId);
    }


}
