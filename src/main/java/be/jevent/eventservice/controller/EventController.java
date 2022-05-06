package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateEventResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.service.EventService;
import be.jevent.eventservice.service.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

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
    public ResponseEntity<List<String>> getAllEventTypes(){
        return new ResponseEntity<>(eventTypeService.getAllEventTypes(), HttpStatus.OK);
    }

    @GetMapping("/publish/{id}")
    public String postMessage(@PathVariable("id") Long id){
        EventDTO eventDTO = eventService.getEventById(id);
        kafkaTemplate.send(TOPIC, eventDTO);
        return "sended Message";
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") Long id){
        return new ResponseEntity<>(eventService.getEventById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDTO>> getEventsByType(@RequestParam String type){
        return new ResponseEntity<>(eventService.getAllEventsByType(EventType.valueOf(type.toUpperCase())), HttpStatus.OK);
    }

}
