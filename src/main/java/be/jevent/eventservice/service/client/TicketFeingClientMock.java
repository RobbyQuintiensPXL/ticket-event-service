package be.jevent.eventservice.service.client;

import org.springframework.stereotype.Component;

@Component
public class TicketFeingClientMock implements TicketFeignClient{
    @Override
    public int getTicketsSold(Long eventId) {
        return 2;
    }
}
