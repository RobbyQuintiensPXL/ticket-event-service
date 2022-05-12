package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByEventName_AndLocation_City(String name, String city);
    List<Event> findAllByEventType(EventType type);
    List<Event> findAllByEventType_AndLocation_City(EventType type, String city);
    List<Event> findAllByTicketOffice_Email(String email);
    List<Event> findAllByEventTypeAndTicketOffice_Email(EventType type, String email);

//    @Query("SELECT e FROM Event e WHERE e.location.city LIKE %?1%")
//    List<Event> findByCity(String city);

    //UPDATE

}
