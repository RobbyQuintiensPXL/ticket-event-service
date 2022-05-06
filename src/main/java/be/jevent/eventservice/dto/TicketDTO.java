package be.jevent.eventservice.dto;


import javax.persistence.Id;

public class TicketDTO{

    @Id
    private Long id;
    private Long eventId;
    private int amountTickets;
    private int ticketNumber;

    public TicketDTO() {
        // Empty constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public int getAmountOfTickets() {
        return amountTickets;
    }

    public void setAmountOfTickets(int amountOfTickets) {
        this.amountTickets = amountOfTickets;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
