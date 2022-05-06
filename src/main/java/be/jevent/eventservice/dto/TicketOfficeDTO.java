package be.jevent.eventservice.dto;

import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;
import java.util.stream.Collectors;

public class TicketOfficeDTO {

    private final Long id;
    private final String email;
    private final String organisation;
    private final Set<LocationDTO> locations;

    public TicketOfficeDTO(TicketOffice ticketOffice){
        this.id = ticketOffice.getId();
        this.email = ticketOffice.getEmail();
        this.organisation = ticketOffice.getOrganisation();
        this.locations = ticketOffice.getLocations().stream().map(LocationDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganisation() {
        return organisation;
    }

    public Set<LocationDTO> getLocations() {
        return locations;
    }
}
