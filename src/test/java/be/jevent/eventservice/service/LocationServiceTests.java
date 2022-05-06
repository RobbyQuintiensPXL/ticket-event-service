package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import be.jevent.eventservice.repository.LocationRepository;
import be.jevent.eventservice.repository.TicketOfficeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
    TicketOfficeRepository ticketOfficeRepository;

    @Autowired
    private LocationService locationService;

    private Location location;
    private TicketOffice ticketOffice;

    public void init(){
        location = new Location();
        location.setBuildingName("Building");
        ticketOffice = new TicketOffice();
        ticketOffice.setId(1L);
        ticketOffice.setOrganisation("Organisation");
        location.setTicketOffice(ticketOffice);
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
    public void getLocationsByTicketOfficeTest(){
        init();
        List<Location> locationList = new LinkedList<>();
        locationList.add(location);

        when(locationRepository.findAllByTicketOffice_Id(ticketOffice.getId())).thenReturn(locationList);

        assertEquals(1, locationList.size());
        assertEquals(ticketOffice.getOrganisation(), locationList.get(0).getTicketOffice().getOrganisation());
        assertEquals(location.getBuildingName(), locationList.get(0).getBuildingName());
    }

    @Test
    public void createLocationTest(){
        init();
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(ticketOfficeRepository.findById(ticketOffice.getId())).thenReturn(java.util.Optional.ofNullable(ticketOffice));
        CreateLocationResource locationResource =
                new CreateLocationResource(location.getBuildingName(), location.getZipCode(),
                        location.getCity(), location.getAddress(), location.getCountry());

        locationService.createLocation(locationResource, ticketOffice.getEmail());
    }

    @Test
    public void throwExceptionWhenLocationByTicketOfficeNotFound(){
        Long id = 2L;
        Throwable thrown = assertThrows(LocationException.class, () -> locationService.getLocationsByTicketOffice(id));

        assertEquals("No locations found for id " + id, thrown.getMessage());
    }

}
