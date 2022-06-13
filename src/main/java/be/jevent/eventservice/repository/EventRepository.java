package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    void deleteById(Long id);
}
