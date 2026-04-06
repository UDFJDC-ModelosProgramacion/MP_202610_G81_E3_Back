package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(FollowUpService.class)
@ContextConfiguration(classes = MainApplication.class)
class FollowUpServiceTest {

    @Autowired
    private FollowUpService followUpService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<FollowUpEntity> followUpList = new ArrayList<>();
    private PetEntity petEntity;
    private VeterinaryEntity veterinaryEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VetVisitEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from FollowUpEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinaryEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        petEntity = factory.manufacturePojo(PetEntity.class);
        petEntity.setMedicalEvents(new ArrayList<>());
        petEntity.setVaccinationRecords(new ArrayList<>());
        petEntity.setPhotographes(new ArrayList<>());
        entityManager.persist(petEntity);

        veterinaryEntity = factory.manufacturePojo(VeterinaryEntity.class);
        veterinaryEntity.setName("Dr. Test");
        veterinaryEntity.setEmail("dr.test@vet.com");
        veterinaryEntity.setSpecialty("General");
        veterinaryEntity.setFollowUps(new ArrayList<>());
        veterinaryEntity.setShelter(null);
        entityManager.persist(veterinaryEntity);

        for (int i = 0; i < 3; i++) {
            FollowUpEntity followUp = factory.manufacturePojo(FollowUpEntity.class);
            followUp.setObservation("Observación " + i);
            followUp.setVisitDate(LocalDate.now().minusDays(i + 1));
            followUp.setPet(petEntity);
            followUp.setVeterinary(veterinaryEntity);
            followUp.setVetVisits(new ArrayList<>());
            entityManager.persist(followUp);
            followUpList.add(followUp);
        }
    }

    @Test
    void testCreateFollowUpValid() throws IllegalOperationException {
        FollowUpEntity newFollowUp = factory.manufacturePojo(FollowUpEntity.class);
        newFollowUp.setObservation("Mascota en buen estado");
        newFollowUp.setVisitDate(LocalDate.now());
        newFollowUp.setPet(petEntity);
        newFollowUp.setVeterinary(veterinaryEntity);
        newFollowUp.setVetVisits(new ArrayList<>());

        FollowUpEntity result = followUpService.createFollowUp(newFollowUp);

        assertNotNull(result);
        FollowUpEntity entity = entityManager.find(FollowUpEntity.class, result.getId());
        assertEquals(newFollowUp.getObservation(), entity.getObservation());
        assertEquals(newFollowUp.getVisitDate(), entity.getVisitDate());
    }

    @Test
    void testCreateFollowUpObservationNull() {
        assertThrows(IllegalOperationException.class, () -> {
            FollowUpEntity newFollowUp = factory.manufacturePojo(FollowUpEntity.class);
            newFollowUp.setObservation(null);
            newFollowUp.setVisitDate(LocalDate.now());
            newFollowUp.setVetVisits(new ArrayList<>());
            followUpService.createFollowUp(newFollowUp);
        });
    }

    @Test
    void testCreateFollowUpObservationEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            FollowUpEntity newFollowUp = factory.manufacturePojo(FollowUpEntity.class);
            newFollowUp.setObservation("   ");
            newFollowUp.setVisitDate(LocalDate.now());
            newFollowUp.setVetVisits(new ArrayList<>());
            followUpService.createFollowUp(newFollowUp);
        });
    }

    @Test
    void testCreateFollowUpDateInFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            FollowUpEntity newFollowUp = factory.manufacturePojo(FollowUpEntity.class);
            newFollowUp.setObservation("Observación válida");
            newFollowUp.setVisitDate(LocalDate.now().plusDays(5));
            newFollowUp.setVetVisits(new ArrayList<>());
            followUpService.createFollowUp(newFollowUp);
        });
    }

    @Test
    void testGetFollowUps() {
        List<FollowUpEntity> followUps = followUpService.getFollowUps();
        assertFalse(followUps.isEmpty());
        assertEquals(3, followUps.size());
    }

    @Test
    void testGetFollowUpValid() throws EntityNotFoundException {
        FollowUpEntity entity = followUpList.get(0);
        FollowUpEntity found = followUpService.getFollowUp(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getId(), found.getId());
        assertEquals(entity.getObservation(), found.getObservation());
    }

    @Test
    void testGetFollowUpNotFound() {
        assertThrows(EntityNotFoundException.class, () -> followUpService.getFollowUp(0L));
    }

    @Test
    void testUpdateFollowUpValid() throws EntityNotFoundException, IllegalOperationException {
        FollowUpEntity entity = followUpList.get(0);
        FollowUpEntity pojoEntity = factory.manufacturePojo(FollowUpEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setObservation("Mascota con excelente adaptación");
        pojoEntity.setVisitDate(LocalDate.now().minusDays(1));
        pojoEntity.setPet(petEntity);
        pojoEntity.setVeterinary(veterinaryEntity);
        pojoEntity.setVetVisits(new ArrayList<>());

        followUpService.updateFollowUp(entity.getId(), pojoEntity);

        FollowUpEntity resp = entityManager.find(FollowUpEntity.class, entity.getId());
        assertEquals(pojoEntity.getObservation(), resp.getObservation());
        assertEquals(pojoEntity.getVisitDate(), resp.getVisitDate());
    }

    @Test
    void testUpdateFollowUpNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            FollowUpEntity pojoEntity = factory.manufacturePojo(FollowUpEntity.class);
            pojoEntity.setObservation("Observación válida");
            pojoEntity.setVisitDate(LocalDate.now());
            pojoEntity.setVetVisits(new ArrayList<>());
            followUpService.updateFollowUp(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateFollowUpObservationNull() {
        assertThrows(IllegalOperationException.class, () -> {
            FollowUpEntity entity = followUpList.get(0);
            FollowUpEntity pojoEntity = factory.manufacturePojo(FollowUpEntity.class);
            pojoEntity.setObservation(null);
            pojoEntity.setVisitDate(LocalDate.now());
            pojoEntity.setVetVisits(new ArrayList<>());
            followUpService.updateFollowUp(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateFollowUpDateInFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            FollowUpEntity entity = followUpList.get(0);
            FollowUpEntity pojoEntity = factory.manufacturePojo(FollowUpEntity.class);
            pojoEntity.setObservation("Observación válida");
            pojoEntity.setVisitDate(LocalDate.now().plusDays(2));
            pojoEntity.setVetVisits(new ArrayList<>());
            followUpService.updateFollowUp(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteFollowUpValid() throws EntityNotFoundException {
        FollowUpEntity entity = followUpList.get(1);
        followUpService.deleteFollowUp(entity.getId());
        FollowUpEntity deleted = entityManager.find(FollowUpEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteFollowUpNotFound() {
        assertThrows(EntityNotFoundException.class, () -> followUpService.deleteFollowUp(0L));
    }
}