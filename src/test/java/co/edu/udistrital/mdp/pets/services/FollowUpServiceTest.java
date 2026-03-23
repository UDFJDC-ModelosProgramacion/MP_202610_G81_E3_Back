package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.FollowUpRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import(FollowUpService.class)
public class FollowUpServiceTest {

    @Autowired
    private FollowUpService followUpService;

    @Autowired
    private FollowUpRepository followUpRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private FollowUpEntity followUpEntity;

    @BeforeEach
    void setUp() throws IllegalOperationException {
        followUpRepository.deleteAll();

        followUpEntity = factory.manufacturePojo(FollowUpEntity.class);
        followUpEntity.setId(null);
        followUpEntity.setObservation("La mascota se adapta bien al nuevo hogar");
        followUpEntity.setVisitDate(LocalDate.now().minusDays(3));
        followUpEntity.setVeterinary(null);
        followUpEntity.setPet(null);
        followUpEntity = followUpService.createFollowUp(followUpEntity);
    }
    // Tests: createFollowUp

    @Test
    void testCreateFollowUpValid() throws IllegalOperationException {
        FollowUpEntity newFollowUp = new FollowUpEntity();
        newFollowUp.setObservation("Mascota en buen estado general");
        newFollowUp.setVisitDate(LocalDate.now());

        FollowUpEntity result = followUpService.createFollowUp(newFollowUp);

        assertNotNull(result.getId());
        assertEquals("Mascota en buen estado general", result.getObservation());
    }

    @Test
    void testCreateFollowUpObservationNull() {
        FollowUpEntity newFollowUp = new FollowUpEntity();
        newFollowUp.setObservation(null);
        newFollowUp.setVisitDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> followUpService.createFollowUp(newFollowUp));
    }

    @Test
    void testCreateFollowUpObservationEmpty() {
        FollowUpEntity newFollowUp = new FollowUpEntity();
        newFollowUp.setObservation("   ");
        newFollowUp.setVisitDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> followUpService.createFollowUp(newFollowUp));
    }

    @Test
    void testCreateFollowUpDateInFuture() {
        FollowUpEntity newFollowUp = new FollowUpEntity();
        newFollowUp.setObservation("Observación válida");
        newFollowUp.setVisitDate(LocalDate.now().plusDays(5));

        assertThrows(IllegalOperationException.class, () -> followUpService.createFollowUp(newFollowUp));
    }

    // Tests: getFollowUps

    @Test
    void testGetFollowUps() {
        List<FollowUpEntity> followUps = followUpService.getFollowUps();
        assertFalse(followUps.isEmpty());
    }

    // Tests: getFollowUp

    @Test
    void testGetFollowUpValid() throws EntityNotFoundException {
        FollowUpEntity found = followUpService.getFollowUp(followUpEntity.getId());
        assertNotNull(found);
        assertEquals(followUpEntity.getId(), found.getId());
    }

    @Test
    void testGetFollowUpNotFound() {
        assertThrows(EntityNotFoundException.class, () -> followUpService.getFollowUp(0L));
    }

    // Tests: updateFollowUp

    @Test
    void testUpdateFollowUpValid() throws EntityNotFoundException, IllegalOperationException {
        FollowUpEntity updated = new FollowUpEntity();
        updated.setObservation("Mascota con excelente adaptación");
        updated.setVisitDate(LocalDate.now().minusDays(1));

        FollowUpEntity result = followUpService.updateFollowUp(followUpEntity.getId(), updated);

        assertEquals("Mascota con excelente adaptación", result.getObservation());
    }

    @Test
    void testUpdateFollowUpNotFound() {
        FollowUpEntity updated = new FollowUpEntity();
        updated.setObservation("Observación válida");
        updated.setVisitDate(LocalDate.now());

        assertThrows(EntityNotFoundException.class, () -> followUpService.updateFollowUp(0L, updated));
    }

    @Test
    void testUpdateFollowUpObservationNull() {
        FollowUpEntity updated = new FollowUpEntity();
        updated.setObservation(null);
        updated.setVisitDate(LocalDate.now());

        assertThrows(IllegalOperationException.class,
                () -> followUpService.updateFollowUp(followUpEntity.getId(), updated));
    }

    @Test
    void testUpdateFollowUpDateInFuture() {
        FollowUpEntity updated = new FollowUpEntity();
        updated.setObservation("Observación válida");
        updated.setVisitDate(LocalDate.now().plusDays(2));

        assertThrows(IllegalOperationException.class,
                () -> followUpService.updateFollowUp(followUpEntity.getId(), updated));
    }

    // Tests: deleteFollowUp

    @Test
    void testDeleteFollowUpValid() throws EntityNotFoundException {
        followUpService.deleteFollowUp(followUpEntity.getId());
        assertThrows(EntityNotFoundException.class, () -> followUpService.getFollowUp(followUpEntity.getId()));
    }

    @Test
    void testDeleteFollowUpNotFound() {
        assertThrows(EntityNotFoundException.class, () -> followUpService.deleteFollowUp(0L));
    }
}