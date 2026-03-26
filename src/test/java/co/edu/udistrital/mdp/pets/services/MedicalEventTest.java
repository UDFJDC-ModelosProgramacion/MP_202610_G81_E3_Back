package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicalEventService.class)
@ContextConfiguration(classes = MainApplication.class)
class MedicalEventServiceTest {

    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MedicalEventEntity> medicalEventList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager()
                .createQuery("delete from MedicalEventEntity")
                .executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MedicalEventEntity entity = factory.manufacturePojo(MedicalEventEntity.class);
            entity.setDescription("Descripcion evento " + i);
            entity.setDate(LocalDate.now());
            entityManager.persist(entity);
            medicalEventList.add(entity);
        }
    }

    @Test
    void testCreateMedicalEvent() throws Exception {
        MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
        newEntity.setDescription("Consulta general");
        newEntity.setDate(LocalDate.now());

        MedicalEventEntity result = medicalEventService.createMedicalEvent(newEntity);
        assertNotNull(result);

        MedicalEventEntity stored = entityManager.find(MedicalEventEntity.class, result.getId());
        assertNotNull(stored);
        assertEquals(newEntity.getDescription(), stored.getDescription());
        assertEquals(newEntity.getDate(), stored.getDate());
    }

    @Test
    void testCreateMedicalEventNullDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription(null);
            newEntity.setDate(LocalDate.now());
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    @Test
    void testCreateMedicalEventEmptyDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription("");
            newEntity.setDate(LocalDate.now());
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    @Test
    void testCreateMedicalEventNullDate() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription("Consulta general");
            newEntity.setDate(null);
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    @Test
    void testUpdateMedicalEvent() throws Exception {
        MedicalEventEntity existing = medicalEventList.get(0);

        MedicalEventEntity updatedData = factory.manufacturePojo(MedicalEventEntity.class);
        updatedData.setDescription("Descripcion actualizada");
        updatedData.setDate(LocalDate.now().plusDays(5));

        MedicalEventEntity result = medicalEventService.updateMedicalEventEntity(existing.getId(), updatedData);
        assertNotNull(result);

        MedicalEventEntity stored = entityManager.find(MedicalEventEntity.class, existing.getId());
        assertEquals("Descripcion actualizada", stored.getDescription());
        assertEquals(updatedData.getDate(), stored.getDate());
    }

    @Test
    void testUpdateMedicalEventOnlyDescription() throws Exception {
        MedicalEventEntity existing = medicalEventList.get(1);
        LocalDate originalDate = existing.getDate();

        MedicalEventEntity partialUpdate = new MedicalEventEntity();
        partialUpdate.setDescription("Solo descripcion actualizada");

        medicalEventService.updateMedicalEventEntity(existing.getId(), partialUpdate);

        MedicalEventEntity stored = entityManager.find(MedicalEventEntity.class, existing.getId());
        assertEquals("Solo descripcion actualizada", stored.getDescription());
        assertEquals(originalDate, stored.getDate());
    }

    @Test
    void testUpdateInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            MedicalEventEntity updatedData = factory.manufacturePojo(MedicalEventEntity.class);
            medicalEventService.updateMedicalEventEntity(0L, updatedData);
        });
    }

    @Test
    void testDeleteMedicalEvent() throws Exception {
        MedicalEventEntity entity = medicalEventList.get(0);
        medicalEventService.deleteMedicalEvent(entity.getId());

        MedicalEventEntity deleted = entityManager.find(MedicalEventEntity.class, entity.getId());
        assertNull(deleted);
    }

@Test
    void testDeleteInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            medicalEventService.deleteMedicalEvent(0L);
        });
    } 
}