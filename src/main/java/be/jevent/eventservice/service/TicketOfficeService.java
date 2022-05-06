package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateTicketOfficeResource;
import be.jevent.eventservice.dto.TicketOfficeDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.exception.TicketOfficeException;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import be.jevent.eventservice.repository.LocationRepository;
import be.jevent.eventservice.repository.TicketOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketOfficeService {

    @Autowired
    private TicketOfficeRepository ticketOfficeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MessageSource messageSource;

    public List<TicketOfficeDTO> getAllTicketOffices() {
        List<TicketOfficeDTO> ticketOfficeDTOList = ticketOfficeRepository.findAll().stream().map(TicketOfficeDTO::new).collect(Collectors.toList());
        if (ticketOfficeDTOList.isEmpty()) {
            throw new TicketOfficeException("No ticket offices found");
        }
        return ticketOfficeDTOList;
    }

    public String createTicketOffice(CreateTicketOfficeResource ticketOfficeResource, Locale locale) {
        Optional<Location> location = locationRepository.findById((long) ticketOfficeResource.getLocationId());

        if (location.isEmpty()) {
            throw new LocationException("No locations found");
        }

        String responseMessage;

        responseMessage = String.format(messageSource.getMessage(
                        "ticketoffice.create.message", null, locale),
                ticketOfficeResource.toString());

        TicketOffice ticketOffice = new TicketOffice();
        ticketOffice.setOrganisation(ticketOfficeResource.getOrganisation());
        ticketOffice.setLocations((Set<Location>) location.get());

        ticketOfficeRepository.save(ticketOffice);

        return responseMessage;
    }

    public TicketOffice getTicketOfficeByUsername(String user) {
        Optional<TicketOffice> ticketOffice = ticketOfficeRepository.findByEmail(user);
        if (ticketOffice.isEmpty()) {
            throw new TicketOfficeException("No Ticket Office found.");
        }
        return ticketOffice.get();
    }


}
