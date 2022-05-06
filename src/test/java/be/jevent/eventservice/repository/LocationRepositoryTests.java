package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Location;
import be.jevent.eventservice.model.TicketOffice;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class LocationRepositoryTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;

    private Location location;

    public void persist(){
        location = new Location();
        location.setAddress("Adres1");
        location.setZipCode(5555);
        location.setCity("Gemeente");
        location.setCountry("Belgium");
        location.setBuildingName("BuildingName");
        entityManager.persist(location);
        entityManager.flush();
    }

    @Test
    public void showAllLocationsTest(){
        persist();
        List<Location> locationList = locationRepository.findAll();

        assertThat(locationList).isNotEmpty();
        assertThat(locationList.get(0).getAddress()).isEqualTo(location.getAddress());
    }

    @Test
    public void showLocationByIdTest(){
        persist();
        Optional<Location> foundLocation = locationRepository.findById(location.getId());

        assertThat(foundLocation).isNotNull();
        assertThat(foundLocation.get().getCountry()).isEqualTo(location.getCountry());
    }

    @Test
    public void showAllLocationsByTicketOfficeTest(){
        persist();
        TicketOffice ticketOffice = new TicketOffice();
        ticketOffice.setOrganisation("Organisation");
        location.setTicketOffice(ticketOffice);

        entityManager.persist(ticketOffice);
        entityManager.flush();


        List<Location> locationList = locationRepository.findAllByTicketOffice_Id(ticketOffice.getId());

        assertThat(locationList).isNotEmpty();
        assertThat(locationList.get(0).getTicketOffice().getOrganisation()).isEqualTo(ticketOffice.getOrganisation());
    }

}
