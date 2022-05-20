package be.jevent.eventservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ticket-service:8082")
public interface TicketFeignClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/tickets/ticket/{eventId}/ticketsleft",
            consumes = "application/json")
    int getTicketsSold(@PathVariable("eventId") Long eventId);
}
