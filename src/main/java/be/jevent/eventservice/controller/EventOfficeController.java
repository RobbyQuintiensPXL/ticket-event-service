package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.filter.UserNameFilter;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.service.EventService;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
        eventService.createEvent(eventResource, banner, thumb, getUser(token));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestHeader HttpHeaders token) {
        return new ResponseEntity<>(eventService.getAllEventsFromTicketOffice(getUser(token)), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> getEventsByType(@RequestHeader HttpHeaders token, @RequestParam String type) {
        return new ResponseEntity<>(eventService.getAllEventsFromTicketOfficeAndType(getUser(token), EventType.valueOf(type.toUpperCase())), HttpStatus.OK);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.deleteEvent(id), HttpStatus.OK);
    }

    private String getUser(HttpHeaders token) {
        UserNameFilter filter = new UserNameFilter();
        return filter.getUsername(token);
    }
}
