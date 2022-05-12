package be.jevent.eventservice.createresource;


import javax.validation.constraints.NotNull;

public class CreateTicketOfficeResource {

    @NotNull
    private final String organisation;

    @NotNull
    private final int locationId;

    public CreateTicketOfficeResource(@NotNull String organisation, @NotNull int locationId) {
        this.organisation = organisation;
        this.locationId = locationId;
    }

    public String getOrganisation() {
        return organisation;
    }

    public int getLocationId() {
        return locationId;
    }
}
