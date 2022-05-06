package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class TicketOfficeRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TicketOfficeRepository ticketOfficeRepository;

    private TicketOffice ticketOffice;

    public void persist(){
        ticketOffice = new TicketOffice();
        ticketOffice.setOrganisation("Office");
        entityManager.persist(ticketOffice);
        entityManager.flush();
    }

    @Test
    public void getAllTicketOfficesTest(){
        persist();
        List<TicketOffice> ticketOfficeList = ticketOfficeRepository.findAll();

        assertThat(ticketOfficeList).isNotEmpty();
        assertThat(ticketOfficeList.get(0).getOrganisation()).isEqualTo(ticketOffice.getOrganisation());
    }

    @Test
    public void getTicketOfficeByIdTest(){
        persist();
        Optional<TicketOffice> foundTickerOffice = ticketOfficeRepository.findById(ticketOffice.getId());

        assertThat(foundTickerOffice).isNotNull();
        assertThat(foundTickerOffice.get().getOrganisation()).isEqualTo(ticketOffice.getOrganisation());
    }

    @Test
    public void getTicketOfficeByIdName(){
        persist();
        Optional<TicketOffice> foundTickerOffice = ticketOfficeRepository.findByOrganisation(ticketOffice.getOrganisation());

        assertThat(foundTickerOffice).isNotNull();
        assertThat(foundTickerOffice.get().getId()).isEqualTo(ticketOffice.getId());
    }

}
