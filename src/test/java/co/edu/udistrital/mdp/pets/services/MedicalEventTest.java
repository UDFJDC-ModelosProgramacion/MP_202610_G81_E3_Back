package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(MedicalEventService.class)
class MedicalEventServiceTest {

    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicalEventEntity> medicalEventList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager()
                .createQuery("delete from MedicalEventEntity")
                .executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MedicalEventEntity entity = factory.manufacturePojo(MedicalEventEntity.class);
            entity.setDescription("Descripcion evento " + i);
            entity.setDate(LocalDate.now());
            entityManager.persist(entity);
            medicalEventList.add(entity);
        }
    }


    /**
     * Prueba para crear un MedicalEvent con datos válidos.
     */
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

    /**
     * Prueba para crear un MedicalEvent con description nula.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMedicalEventNullDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription(null);
            newEntity.setDate(LocalDate.now());
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    /**
     * Prueba para crear un MedicalEvent con description vacía.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMedicalEventEmptyDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription("");
            newEntity.setDate(LocalDate.now());
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    /**
     * Prueba para crear un MedicalEvent con date nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMedicalEventNullDate() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity newEntity = factory.manufacturePojo(MedicalEventEntity.class);
            newEntity.setDescription("Consulta general");
            newEntity.setDate(null);
            medicalEventService.createMedicalEvent(newEntity);
        });
    }

    /**
     * Prueba para actualizar un MedicalEvent existente con todos los campos.
     */
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

    /**
     * Prueba para actualizar solo la description.
     * Los campos nulos no deben modificar los valores originales.
     */
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

    /**
     * Prueba para actualizar un MedicalEvent que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testUpdateInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            MedicalEventEntity updatedData = factory.manufacturePojo(MedicalEventEntity.class);
            medicalEventService.updateMedicalEventEntity(0L, updatedData);
        });
    }



    /**
     * Prueba para eliminar un MedicalEvent existente.
     */
    @Test
    void testDeleteMedicalEvent() throws Exception {
        MedicalEventEntity entity = medicalEventList.get(0);
        medicalEventService.delateMedicalEvent(entity.getId());

        MedicalEventEntity deleted = entityManager.find(MedicalEventEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un MedicalEvent que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testDeleteInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            medicalEventService.delateMedicalEvent(0L);
        });
    }
}