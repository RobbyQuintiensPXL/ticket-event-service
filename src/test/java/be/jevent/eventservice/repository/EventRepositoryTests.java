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

    @Autowired
    private EventPageRepository eventPageRepository;

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
    public void showAllEventsTest(){
        persist();
        Pageable paging = PageRequest.of(0, 5);
        Page<EventDTO> eventList = eventPageRepository.findAll(paging).map(EventDTO::new);

        List<EventDTO> eventDTOList = eventList.get().collect(Collectors.toList());

        assertThat(eventList).isNotEmpty();
        assertThat(eventDTOList.get(0).getDescription()).isEqualTo(event.getDescription());
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
        event.setTicketOffice("Organisation");

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

    @Test
    public void showAllEventsByTicketOfficeEmailTest(){
        persist();
        List<Event> eventList = eventRepository.
                findAllByTicketOffice(event.getTicketOffice());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }

    @Test
    public void showAllEventsByTicketOfficeEmailAndTypeTest(){
        persist();
        List<Event> eventList = eventRepository.
                findAllByEventTypeAndTicketOffice(EventType.valueOf("concert".toUpperCase()), event.getTicketOffice());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }

    @Test
    public void showAllEventsByLocationCityTest(){
        persist();
        Location location = new Location();
        location.setCity("City");
        location.setBuildingName("Building");
        event.setLocation(location);

        entityManager.persist(location);
        entityManager.flush();

        List<Event> eventList = eventRepository.
                findAllByLocation_City(event.getLocation().getCity());

        assertThat(eventList).isNotEmpty();
        assertThat(eventList.get(0).getDescription()).isEqualTo(event.getDescription());
    }
}
