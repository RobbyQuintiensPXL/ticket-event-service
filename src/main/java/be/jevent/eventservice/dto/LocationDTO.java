package be.jevent.eventservice.dto;

import be.jevent.eventservice.model.Location;

public class LocationDTO {

    private final Long id;
    private final String buildingName;
    private final int zipCode;
    private final String city;
    private final String address;
    private final String country;
    private final String ticketOffice;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.buildingName = location.getBuildingName();
        this.zipCode = location.getZipCode();
        this.city = location.getCity();
        this.address = location.getAddress();
        this.country = location.getCountry();
        this.ticketOffice = location.getTicketOffice().getOrganisation();
    }

    public Long getId() {
        return id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getTicketOffice() {
        return ticketOffice;
    }
}
