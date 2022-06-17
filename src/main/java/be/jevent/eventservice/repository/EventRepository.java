package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    void deleteById(Long id);

    List<Event> findAllByEventDateAfter(LocalDate date);
}
