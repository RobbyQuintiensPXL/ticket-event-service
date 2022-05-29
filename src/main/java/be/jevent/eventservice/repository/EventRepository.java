package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> deleteByIdAndTicketOffice(Long id, String email);
}
