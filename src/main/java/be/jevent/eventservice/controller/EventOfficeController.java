package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.filter.UserNameFilter;
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
                                            @RequestPart(value = "banner") MultipartFile banner,
                                            @RequestPart(value = "thumb") MultipartFile thumb) throws IOException, FileUploadException {
        UserNameFilter filter = new UserNameFilter();
        String user = filter.getUsername(token);
        eventService.createEvent(eventResource, banner, thumb, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestHeader HttpHeaders token) {
        UserNameFilter filter = new UserNameFilter();
        String user = filter.getUsername(token);

        return new ResponseEntity<>(eventService.getAllEventsFromTicketOffice(user), HttpStatus.OK);
    }


    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.deleteEvent(id), HttpStatus.OK);
    }
}
