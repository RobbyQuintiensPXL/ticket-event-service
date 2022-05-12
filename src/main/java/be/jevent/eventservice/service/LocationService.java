package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isEmpty()) {
            throw new LocationException("Location not found");
        }
        return location.get();
    }

    public List<LocationDTO> getAllLocations() {
        List<LocationDTO> locationDTOList = locationRepository.findAll().stream().map(LocationDTO::new).collect(Collectors.toList());
        if (locationDTOList.isEmpty()) {
            throw new LocationException("No locations found");
        }
        return locationDTOList;
    }

    public List<LocationDTO> getLocationsByTicketOffice(String ticketOffice) {
        List<LocationDTO> locationDTOList = locationRepository.findAllByTicketOffice(ticketOffice).stream().map(LocationDTO::new).collect(Collectors.toList());
        if (locationDTOList.isEmpty()) {
            throw new LocationException("No locations found");
        }
        return locationDTOList;
    }

    public void createLocation(CreateLocationResource locationResource, String ticketOffice) {
        Location location = new Location();
        location.setBuildingName(locationResource.getBuildingName());
        location.setZipCode(locationResource.getZipCode());
        location.setCity(locationResource.getCity());
        location.setAddress(locationResource.getAddress());
        location.setCountry(locationResource.getCountry());
        location.setTicketOffice(ticketOffice);

        locationRepository.save(location);
    }
}
