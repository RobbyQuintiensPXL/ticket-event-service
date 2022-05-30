package be.jevent.eventservice.service;

import be.jevent.eventservice.createresource.CreateLocationResource;
import be.jevent.eventservice.dto.LocationDTO;
import be.jevent.eventservice.exception.LocationException;
import be.jevent.eventservice.model.Event;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.repository.EventRepository;
import be.jevent.eventservice.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    public LocationService(LocationRepository locationRepository,
                           EventRepository eventRepository) {
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
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

    public List<String> getAllLocationCities() {
        List<String> cityList = eventRepository.findAll().stream().map(Event::getLocation).map(Location::getCity).distinct().collect(Collectors.toList());
        if (cityList.isEmpty()) {
            throw new LocationException("No cities found");
        }
        return cityList;
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
