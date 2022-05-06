package be.jevent.eventservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("localhost:9080")
public interface TicketOfficeFeignClient {

    @RequestMapping(
            method= RequestMethod.GET,
            value="/token",
            consumes="application/json")
    String getEmail();
}
