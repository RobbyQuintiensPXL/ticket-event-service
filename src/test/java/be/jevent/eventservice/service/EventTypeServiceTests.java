package be.jevent.eventservice.service;

import be.jevent.eventservice.model.EventType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventTypeServiceTests {

    @Autowired
    private EventTypeService eventTypeService;

    private EventType eventType;

    @Test
    public void getAllEventsTest() {
        List<String> eventTypes = Stream.of(EventType.values()).map(EventType::getType).collect(Collectors.toList());

        List<String> eventTypesFromService = eventTypeService.getAllEventTypes();


        assertEquals(eventTypes.size(), eventTypesFromService.size());
        assertEquals(eventTypes.get(0), eventTypesFromService.get(0));
        assertEquals(eventTypes.get(1), eventTypesFromService.get(1));
        assertEquals(eventTypes.get(2), eventTypesFromService.get(2));
        assertEquals(eventTypes.get(3), eventTypesFromService.get(3));
    }

    @Test
    public void throwExceptionNoEventTypesFound() {
        eventTypeService = mock(EventTypeService.class);
        when(eventTypeService.getAllEventTypes()).thenReturn(null);
    }
}
