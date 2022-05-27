package be.jevent.eventservice.filter;

import be.jevent.eventservice.model.QEvent;
import com.querydsl.core.types.Predicate;

public class EventPredicates {
    private EventPredicates() {}

    public static Predicate eventsByTicketOffice(String ticketoffce){
        if(ticketoffce == null || ticketoffce.isEmpty()){
            return QEvent.event.isNotNull();
        } else {
            return QEvent.event.ticketOffice.containsIgnoreCase(ticketoffce)
                    .or(QEvent.event.location.buildingName.containsIgnoreCase(ticketoffce));
        }
    }
}