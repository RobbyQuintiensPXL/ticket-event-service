package be.jevent.eventservice.createresource;

import javax.validation.constraints.NotNull;

public class CreateLocationResource {

    @NotNull
    private final String buildingName;

    @NotNull
    private final int zipCode;

    @NotNull
    private final String city;

    @NotNull
    private final String address;

    @NotNull
    private final String country;

    public CreateLocationResource(@NotNull String buildingName, @NotNull int zipCode,
                                  @NotNull String city, @NotNull String address,
                                  @NotNull String country) {
        this.buildingName = buildingName;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
        this.country = country;
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

    @Override
    public String toString() {
        return "CreateLocationResource{" +
                "buildingName='" + buildingName + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
