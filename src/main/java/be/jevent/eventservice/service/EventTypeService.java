package be.jevent.eventservice.service;

import be.jevent.eventservice.exception.EventException;
import be.jevent.eventservice.model.EventType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventTypeService {


    public List<String> getAllEventTypes() {
        List<String> eventTypes = Stream.of(EventType.values()).map(EventType::getType).collect(Collectors.toList());
        if (eventTypes.isEmpty()) {
            throw new EventException("No event types found");
        }
        return eventTypes;
    }
}
