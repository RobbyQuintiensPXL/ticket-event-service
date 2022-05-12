package be.jevent.eventservice.repository;

import be.jevent.eventservice.model.Location;
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
        assertThat(foundLocation.get().getZipCode()).isEqualTo(location.getZipCode());
        assertThat(foundLocation.get().getAddress()).isEqualTo(location.getAddress());
    }

    @Test
    public void showAllLocationsByTicketOfficeTest(){
        persist();

        List<Location> locationList = locationRepository.findAllByTicketOffice(location.getTicketOffice());

        assertThat(locationList).isNotEmpty();
        assertThat(locationList.get(0).getTicketOffice()).isEqualTo(location.getTicketOffice());
        assertThat(locationList.get(0).getBuildingName()).isEqualTo(location.getBuildingName());
        assertThat(locationList.get(0).getCity()).isEqualTo(location.getCity());
    }

}
