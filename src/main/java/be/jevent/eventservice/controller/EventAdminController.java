package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.service.EventService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "admin")
public class EventAdminController {

    private final EventService eventService;

    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size,
                                                       @QuerydslPredicate(root = Event.class) Predicate predicate) {
        return new ResponseEntity<>(eventService.getNewEventsForAdmin(predicate, page, size), HttpStatus.OK);
    }

    @PostMapping("/event/{id}/approve")
    public ResponseEntity<Void> approveEvent(@PathVariable("id") Long id) {
        eventService.approveEvent(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.deleteEvent(id), HttpStatus.OK);
    }
}
