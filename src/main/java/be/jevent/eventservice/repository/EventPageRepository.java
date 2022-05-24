package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventPageRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAll(Pageable pageable);


/*    @Query("FROM Event WHERE eventName = ?1 OR ")
    Page<Event> findAllByEventNameAndEventTypeAndLocation_City(String name, String type, String city);*/
}
