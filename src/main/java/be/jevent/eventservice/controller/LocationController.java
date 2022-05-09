package be.jevent.eventservice.controller;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.filter.UserNameFilter;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
    }

    @PostMapping(value = "/add_location")
    public ResponseEntity<Void> createLocation(@RequestHeader HttpHeaders token,
                                               @RequestBody @Valid CreateLocationResource locationResource) {
        UserNameFilter filter = new UserNameFilter();
        String user = filter.getUsername(token);
        locationService.createLocation(locationResource, user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "office")
    public ResponseEntity<List<LocationDTO>> getLocationsByTicketOffice(@RequestHeader HttpHeaders token) {
        UserNameFilter filter = new UserNameFilter();
        String user = filter.getUsername(token);

        return new ResponseEntity<>(locationService.getLocationsByTicketOfficeEmail(user), HttpStatus.OK);
    }
}
