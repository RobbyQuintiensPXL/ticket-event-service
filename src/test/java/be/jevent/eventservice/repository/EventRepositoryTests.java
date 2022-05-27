package be.jevent.eventservice.repository;

import be.jevent.eventservice.dto.EventDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private Location location;

    public void persist(){
        location = new Location();
        location.setCity("TestCity");
        entityManager.persist(location);
        event = new Event();
        event.setEventName("EventName");
        event.setEventType(EventType.CONCERT);
        event.setDescription("Description");
        event.setTicketOffice("ticketOffice");
        event.setEventTime(LocalTime.of(20,10,0));
        event.setLocation(location);
        entityManager.persist(event);
        entityManager.flush();
    }


    @Test
    public void showAllEventsByTicketOfficeEmailAndTypeTest(){
        persist();
        List<Event> eventList = eventRepository.
                findAllByEventTypeAndTicketOffice(EventType.valueOf("concert".toUpperCase()), event.getTicketOffice());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }
}
