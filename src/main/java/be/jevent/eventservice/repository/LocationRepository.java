package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByTicketOffice_Id(Long id);

    List<Location> findAllByTicketOffice_Email(String email);

}
