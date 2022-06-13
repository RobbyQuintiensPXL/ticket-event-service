package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.filter.UserNameFilter;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.service.EventService;
import com.querydsl.core.types.Predicate;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "office")
public class EventOfficeController {

    private final EventService eventService;

    public EventOfficeController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/event/post", consumes = {"*/*"})
    public ResponseEntity<Void> createEvent(@RequestHeader HttpHeaders token,
                                            @RequestPart @Valid CreateEventResource eventResource,
                                            @RequestPart MultipartFile banner,
                                            @RequestPart MultipartFile thumb) throws IOException, FileUploadException {
        eventService.createEvent(eventResource, banner, thumb, getTicketOffice(token));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @QuerydslPredicate(root = Event.class) Predicate predicate,
                                                       @RequestHeader HttpHeaders token) {
        return new ResponseEntity<>(eventService.getAllEventsFromTicketOffice(predicate, getTicketOffice(token), page, size), HttpStatus.OK);
    }

    private String getTicketOffice(HttpHeaders token) {
        UserNameFilter filter = new UserNameFilter();
        return filter.getTicketOffice(token);
    }
}
