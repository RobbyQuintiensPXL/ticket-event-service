package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "events")
public class EventController {

    private final static LocalDate LOCAL_DATE = LocalDate.now();

    private final EventService eventService;
    private final EventTypeService eventTypeService;

    public EventController(final EventService eventService,
                           final EventTypeService eventTypeService) {
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllEventTypes() {
        return new ResponseEntity<>(eventTypeService.getAllEventTypes(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "5") int size) {
        return new ResponseEntity<>(eventService.getAllEventsImpl(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventDTO>> getEventsByTypeAndCity(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size,
                                                                 @QuerydslPredicate(root = Event.class) Predicate predicate) {
        return new ResponseEntity<>(eventService.findByTypeCity(predicate, page, size), HttpStatus.OK);
    }
}
