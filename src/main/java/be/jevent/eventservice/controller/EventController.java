package be.jevent.eventservice.controller;

import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import org.springframework.data.domain.Page;
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
        return new ResponseEntity<>(eventService.getAllEvents(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<EventDTO>> getEventsByType(@RequestParam String type) {
        return new ResponseEntity<>(eventService.getAllEventsByType(EventType.valueOf(type.toUpperCase())),
                HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> getEventsByTypeAndCity(@RequestParam(required = false) String type,
                                                                 @RequestParam(required = false) String city) {
        if (type == null) {
            return new ResponseEntity<>(eventService.getEventsByCity(city), HttpStatus.OK);
        } else if (city == null) {
            return new ResponseEntity<>(eventService.getAllEventsByType(EventType.valueOf(type.toUpperCase())), HttpStatus.OK);
        }

        return new ResponseEntity<>(eventService.getAllEventsByTypeAndCity(EventType.valueOf(type.toUpperCase()), city),
                HttpStatus.OK);
    }

    @GetMapping("/searchterm")
    public ResponseEntity<Page<EventDTO>> getEventsBySearchTerm(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size,
                                                                @RequestParam(required = false) String search) {
        return new ResponseEntity<>(eventService.findBySearchTerm(search, page, size), HttpStatus.OK);

    }
}
