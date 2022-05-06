package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateTicketOfficeResource;
import be.jevent.eventservice.dto.TicketOfficeDTO;
import be.jevent.eventservice.service.TicketOfficeService;
import be.jevent.eventservice.service.client.TicketOfficeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "ticketoffices")
public class TicketOfficeController {

    private final static String EMAIL_CLAIM = "https://example.com/email";

    @Autowired
    private TicketOfficeFeignClient ticketOfficeFeignClient;

    @Autowired
    private TicketOfficeService ticketOfficeService;

    @GetMapping
    public ResponseEntity<List<TicketOfficeDTO>> getAllTicketOffices() {
        return new ResponseEntity<>(ticketOfficeService.getAllTicketOffices(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createTicketOffice(@RequestBody @Valid CreateTicketOfficeResource ticketOfficeResource,
                                                     Locale locale) {
        return new ResponseEntity<>(ticketOfficeService.createTicketOffice(ticketOfficeResource, locale), HttpStatus.CREATED);
    }
}
