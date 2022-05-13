package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.model.Location;
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

    @Autowired
    private LocationService locationService;

    private Location location;

    public void init(){
        location = new Location();
        location.setId(1L);
        location.setBuildingName("Building");
        location.setTicketOffice("ticketOffice");
        location.setCountry("Belgium");
    }

    @Test
    public void getAllLocationsTest(){
        init();
        List<Location> locationList = new LinkedList<>();
        locationList.add(location);

        when(locationRepository.findAll()).thenReturn(locationList);

        List<LocationDTO> locationDTOList = locationService.getAllLocations();

        assertEquals(locationList.size(), locationDTOList.size());
    }

    @Test
    public void getLocationById(){
        init();

        when(locationRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(this.location));

        Location location = locationService.getLocationById(this.location.getId());
        assertEquals(location.getCountry(), this.location.getCountry());
        assertEquals(location.getId(), this.location.getId());
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
