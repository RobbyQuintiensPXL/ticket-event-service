package be.jevent.eventservice.service;

import be.jevent.eventservice.dto.TicketOfficeDTO;
import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import be.jevent.eventservice.repository.LocationRepository;
import be.jevent.eventservice.repository.TicketOfficeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TicketOfficeServiceTests {

//    @MockBean
//    private LocationRepository locationRepository;
//
//    @MockBean
//    TicketOfficeRepository ticketOfficeRepository;
//
//    @Autowired
//    private TicketOfficeService ticketOfficeService;
//
//    private TicketOffice ticketOffice;
//
//    public void init(){
//        Location location = new Location();
//        location.setAddress("test");
//        ticketOffice.setOrganisation("Organisation");
//        ticketOffice.setId(1L);
//        location.setTicketOffice(ticketOffice);
//    }
//
//    @Test
//    public void getAllTicketOfficesTest(){
//        init();
//        List<TicketOffice> ticketOfficeList = new LinkedList<>();
//        ticketOfficeList.add(ticketOffice);
//        when(ticketOfficeRepository.findAll()).thenReturn(ticketOfficeList);
//
//        List<TicketOfficeDTO> ticketOfficeDTOList = ticketOfficeService.getAllTicketOffices();
//
//        assertEquals(ticketOfficeList.size(), ticketOfficeDTOList.size());
//    }

}
