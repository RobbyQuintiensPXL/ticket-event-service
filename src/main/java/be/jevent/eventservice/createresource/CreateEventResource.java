package be.jevent.eventservice.createresource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class CreateEventResource {

    @NotNull
    private final String eventName;

    @NotNull
    private final String eventType;

    @NotNull
    private final String shortDescription;

    @NotNull
    private final String description;

    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private final LocalDate eventDate;

    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private final LocalTime eventTime;

    @NotNull
    private final int locationId;

    @NotNull
    private final double price;

    @NotNull
    private final int amountOfTickets;

    private final String thumbnail;

    private final String banner;


    public CreateEventResource(@NotNull String eventName, @NotNull String eventType, @NotNull String shortDescription,
                               @NotNull String description, @NotNull LocalDate eventDate, @NotNull LocalTime eventTime,
                               @NotNull int locationId, @NotNull double price, @NotNull int amountOfTickets,
                               String thumbnail, String banner){
        this.eventName = eventName;
        this.eventType = eventType;
        this.shortDescription = shortDescription;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.locationId = locationId;
        this.price = price;
        this.amountOfTickets = amountOfTickets;
        this.thumbnail = thumbnail;
        this.banner = banner;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType.toUpperCase();
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public int getLocationId() {
        return locationId;
    }

    public double getPrice() {
        return price;
    }

    public int getAmountOfTickets() {
        return amountOfTickets;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getBanner() {
        return banner;
    }

    @Override
    public String toString() {
        return "CreateEventResource{" +
                "eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate=" + eventDate +
                ", eventTime=" + eventTime +
                ", location='" + locationId + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
