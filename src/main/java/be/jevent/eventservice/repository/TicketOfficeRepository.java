package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.TicketOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketOfficeRepository extends JpaRepository<TicketOffice, Long> {

    Optional<TicketOffice> findByOrganisation(String organisation);

    Optional<TicketOffice> findByEmail(String user);
}
