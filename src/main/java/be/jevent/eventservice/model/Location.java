package be.jevent.eventservice.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "locations")
@Entity
public class Location implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "zip_code")
    private int zipCode;

    private String city;

    private String address;

    private String ticketOffice;

    private String country;

    public Location() {
        //Empty Contstructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTicketOffice() {
        return ticketOffice;
    }

    public void setTicketOffice(String ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

}
