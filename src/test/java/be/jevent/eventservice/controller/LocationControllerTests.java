package be.jevent.eventservice.controller;


import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.service.LocationService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
public class LocationControllerTests {

    @InjectMocks
    LocationController locationController;

    @Mock
    LocationService locationService;

    private List<Location> locationList;
    private Location location;
    private Location location2;

    public void init() {
        location = new Location();
        location.setId(1L);
        location.setBuildingName("Building");
        location.setCity("City");

        location2 = new Location();
        location2.setId(2L);
        location2.setBuildingName("Building2");
        location2.setCity("City2");

        locationList = new ArrayList<>();
        locationList.add(location);
        locationList.add(location2);
    }

    @Test
    public void getAllLocationsTest() {
        init();
        when(locationService.getAllLocations()).thenReturn(locationList.stream().map(LocationDTO::new).collect(Collectors.toList()));

        ResponseEntity<List<LocationDTO>> responseEntity = locationController.getAllLocations();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getLocationByIdTest() {
        init();
        when(locationService.getLocationById(anyLong())).thenReturn(location);

        ResponseEntity<Location> responseEntity = locationController.getLocationById(location.getId());

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getBuildingName()).isEqualTo(location.getBuildingName());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getCity()).isEqualTo(location.getCity());
    }

    @Test
    public void getAllCitiesTest(){
        init();
        List<String> cityList = new LinkedList<>();
        cityList.add(location.getCity());

        when(locationService.getAllLocationCities()).thenReturn(cityList);

        ResponseEntity<List<String>> responseEntity = locationController.getAllCities();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).get(0)).isEqualTo(location.getCity());
    }
}
