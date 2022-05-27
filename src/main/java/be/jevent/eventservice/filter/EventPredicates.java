package be.jevent.eventservice.filter;

import be.jevent.eventservice.model.QEvent;
import com.querydsl.core.types.Predicate;

public class EventPredicates {
    private EventPredicates() {}

    public static Predicate eventNameOrBuildingNameContainsIgnoreCase(String search) {
        if(search == null || search.isEmpty()){
            return QEvent.event.isNotNull();
        } else {
            return QEvent.event.eventName.containsIgnoreCase(search)
                    .or(QEvent.event.location.buildingName.containsIgnoreCase(search));
        }
    }

    public static Predicate eventsByTicketOffice(String ticketoffce){
        if(ticketoffce == null || ticketoffce.isEmpty()){
            return QEvent.event.isNotNull();
        } else {
            return QEvent.event.ticketOffice.containsIgnoreCase(ticketoffce)
                    .or(QEvent.event.location.buildingName.containsIgnoreCase(ticketoffce));
        }
    }
}