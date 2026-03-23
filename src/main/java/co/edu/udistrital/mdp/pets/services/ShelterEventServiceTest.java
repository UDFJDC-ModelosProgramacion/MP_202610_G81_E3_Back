package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterEventService.class)
@ContextConfiguration(classes = MainApplication.class)
class ShelterEventServiceTest {

    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ShelterEventEntity> eventList = new ArrayList<>();
    private ShelterEntity shelterEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);

        for (int i = 0; i < 3; i++) {
            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setShelter(shelterEntity);
            event.setDate(LocalDate.now().plusDays(i));
            entityManager.persist(event);
            eventList.add(event);
        }
    }

    @Test
    void createShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newEvent.setShelter(shelterEntity);
        newEvent.setDate(LocalDate.now().plusDays(10));

        ShelterEventEntity result = shelterEventService.createShelterEvent(newEvent);

        assertNotNull(result);
        ShelterEventEntity entity = entityManager.find(ShelterEventEntity.class, result.getId());
        assertEquals(newEvent.getName(), entity.getName());
        assertEquals(newEvent.getDate(), entity.getDate());
        assertEquals(newEvent.getDescription(), entity.getDescription());
    }

    @Test
    void createShelterEventWithRepeatedDate() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
            newEvent.setDate(eventList.get(0).getDate());
            newEvent.setShelter(shelterEntity);
            shelterEventService.createShelterEvent(newEvent);
        });
    }

    @Test
    void updateShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        ShelterEventEntity entity = eventList.get(0);
        ShelterEventEntity pojoEntity = factory.manufacturePojo(ShelterEventEntity.class);
        pojoEntity.setShelter(shelterEntity);
        pojoEntity.setDate(LocalDate.now().plusDays(20));

        shelterEventService.updateShelterEventEntity(entity.getId(), pojoEntity);

        ShelterEventEntity resp = entityManager.find(ShelterEventEntity.class, entity.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getDate(), resp.getDate());
        assertEquals(pojoEntity.getDescription(), resp.getDescription());
    }

    @Test
    void updateNonExistingShelterEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
            newEvent.setShelter(shelterEntity);
            newEvent.setDate(LocalDate.now().plusDays(5));
            shelterEventService.updateShelterEventEntity(999L, newEvent);
        });
    }

    @Test
    void deleteShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        ShelterEventEntity entity = eventList.get(1);
        shelterEventService.deleteShelterEvent(entity.getId());
        ShelterEventEntity deleted = entityManager.find(ShelterEventEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void deleteNonExistingShelterEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterEventService.deleteShelterEvent(999L);
        });
    }
}
