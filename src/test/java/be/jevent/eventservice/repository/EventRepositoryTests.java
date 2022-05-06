package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import be.jevent.eventservice.model.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class EventRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    private Event event;

    public void persist(){
        event = new Event();
        event.setEventName("EventName");
        event.setEventType(EventType.CONCERT);
        event.setDescription("Description");
        event.setEventTime(LocalTime.of(20,10,0));
        entityManager.persist(event);
        entityManager.flush();
    }

    @Test
    public void showAllEventsTest(){
        persist();
        List<Event> eventList = eventRepository.findAll();

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }

    @Test
    public void showAllEventsByTypeTest(){
        persist();
        List<Event> eventList = eventRepository.findAllByEventType(EventType.valueOf("concert".toUpperCase()));

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getEventTime()).isEqualTo(event.getEventTime());
    }

    @Test
    public void showAllEventsByCityAndEventNameTest(){
        persist();
        Location location = new Location();
        location.setCity("City");
        location.setBuildingName("Building");
        event.setLocation(location);

        entityManager.persist(location);
        entityManager.flush();

        List<Event> eventList = eventRepository.findAllByEventName_AndLocation_City(event.getEventName(), location.getCity());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getEventDate()).isEqualTo(event.getEventDate());
        assertThat(eventList.get(0).getLocation().getBuildingName()).isEqualTo(event.getLocation().getBuildingName());
    }

    @Test
    public void showAllEventsByTypeAndCityTest(){
        persist();
        Location location = new Location();
        location.setCity("City");
        location.setBuildingName("Building");
        event.setLocation(location);

        entityManager.persist(location);
        entityManager.flush();

        List<Event> eventList = eventRepository.
                findAllByEventType_AndLocation_City(EventType.valueOf("concert".toUpperCase()), event.getLocation().getCity());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }
}
