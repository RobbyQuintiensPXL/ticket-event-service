package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "events")
public class EventController {

    private static final String TOPIC = "events";

    private final KafkaTemplate<String, EventDTO> kafkaTemplate;
    private final EventService eventService;
    private final EventTypeService eventTypeService;

    public EventController(final KafkaTemplate<String, EventDTO> kafkaTemplate,
                           final EventService eventService,
                           final EventTypeService eventTypeService) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventService = eventService;
        this.eventTypeService = eventTypeService;
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAllEventTypes() {
        return new ResponseEntity<>(eventTypeService.getAllEventTypes(), HttpStatus.OK);
    }

    @GetMapping("/publish/{id}")
    public String postMessage(@PathVariable("id") Long id) {
        EventDTO eventDTO = eventService.getEventById(id);
        kafkaTemplate.send(TOPIC, eventDTO);
        return "sended Message";
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

    @GetMapping("/searchterm")
    public ResponseEntity<Page<EventDTO>> getEventsBySearchTerm(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam(required = false) String search) {
        return new ResponseEntity<>(eventService.findBySearchTerm(search, page, size), HttpStatus.OK);
    }
}
