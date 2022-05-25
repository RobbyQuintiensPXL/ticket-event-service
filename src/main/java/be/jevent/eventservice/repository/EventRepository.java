package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByTicketOffice(String email);

    List<Event> findAllByEventTypeAndTicketOffice(EventType type, String email);

}
