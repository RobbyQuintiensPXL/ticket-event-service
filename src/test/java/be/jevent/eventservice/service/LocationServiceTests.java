package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.EventDTO;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.repository.LocationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LocationServiceTests {

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private LocationService locationService;

    private Location location;

    public void init(){
        location = new Location();
        location.setId(1L);
        location.setBuildingName("Building");
        location.setTicketOffice("ticketOffice");
        location.setCountry("Belgium");
        location.setAddress("testAdres");
        location.setCity("city");
    }

    @Test
    public void getAllLocationsTest(){
        init();
        List<Location> locationList = new LinkedList<>();
        locationList.add(location);

        when(locationRepository.findAll()).thenReturn(locationList);

        List<LocationDTO> locationDTOList = locationService.getAllLocations();

        assertEquals(locationList.size(), locationDTOList.size());
        assertEquals(location.getZipCode(), locationDTOList.get(0).getZipCode());
        assertEquals(location.getCity(), locationDTOList.get(0).getCity());
        assertEquals(location.getAddress(), locationDTOList.get(0).getAddress());
    }

    @Test
    public void getAllLocationCitiesTest(){
        init();

        Event event = new Event();
        event.setLocation(location);
        event.setAccepted(false);

        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAll()).thenReturn(eventList);

        List<String> listOfCities = locationService.getAllLocationCities();

        assertEquals(eventList.size(), listOfCities.size());
        assertEquals(eventList.get(0).getLocation().getCity(), listOfCities.get(0));
    }

    @Test
    public void getAllLocationCitiesAcceptedTest(){
        init();

        Event event = new Event();
        event.setLocation(location);
        event.setAccepted(true);

        List<Event> eventList = new LinkedList<>();
        eventList.add(event);

        when(eventRepository.findAll()).thenReturn(eventList);

        List<String> listOfCities = locationService.getAllLocationCities();

        assertEquals(eventList.size(), listOfCities.size());
        assertEquals(eventList.get(0).getLocation().getCity(), listOfCities.get(0));
    }

    @Test
    public void getLocationById(){
        init();

        when(locationRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(this.location));

        Location location = locationService.getLocationById(this.location.getId());
        assertEquals(location.getCountry(), this.location.getCountry());
        assertEquals(location.getId(), this.location.getId());
        assertEquals(location.getTicketOffice(), this.location.getTicketOffice());
    }

    @Test
    public void getLocationsByTicketOfficeTest(){
        init();
        List<Location> locationList = new LinkedList<>();
        locationList.add(location);

        when(locationRepository.findAllByTicketOffice(location.getTicketOffice())).thenReturn(locationList);

        assertEquals(1, locationList.size());
        assertEquals(location.getCountry(), locationList.get(0).getCountry());
        assertEquals(location.getBuildingName(), locationList.get(0).getBuildingName());
    }

    @Test
    public void createLocationTest(){
        init();
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        CreateLocationResource locationResource =
                new CreateLocationResource(location.getBuildingName(), location.getZipCode(),
                        location.getCity(), location.getAddress(), location.getCountry());

        locationService.createLocation(locationResource, anyString());
    }

    @Test
    public void throwExceptionWhenLocationByTicketOfficeNotFound(){
        Throwable thrown = assertThrows(LocationException.class, () -> locationService.getLocationsByTicketOffice("organisation"));

        assertEquals("No locations found", thrown.getMessage());
    }

    @Test
    public void throwExceptionWhenLocationByIDNotFound(){
        Throwable thrown = assertThrows(LocationException.class, () -> locationService.getLocationById(anyLong()));

        assertEquals("Location not found", thrown.getMessage());
    }

    @Test
    public void throwExceptionWhenLocationsNotFound(){
        Throwable thrown = assertThrows(LocationException.class, () -> locationService.getAllLocations());

        assertEquals("No locations found", thrown.getMessage());
    }

}
