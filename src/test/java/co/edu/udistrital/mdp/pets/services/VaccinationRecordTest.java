package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(VaccinationRecordService.class)
class VaccinationRecordTest {

    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VaccinationRecordEntity> vaccinationRecordList = new ArrayList<>();

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
                .createQuery("delete from VaccinationRecordEntity")
                .executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VaccinationRecordEntity entity = factory.manufacturePojo(VaccinationRecordEntity.class);
            entity.setVaccineName("Vaccine_" + i);
            entity.setVaccineDate(LocalDate.now());
            entity.setNextDosesDate(LocalDate.now().plusMonths(1));
            entityManager.persist(entity);
            vaccinationRecordList.add(entity);
        }
    }

 
    /**
     * Prueba para crear un VaccinationRecord con datos válidos.
     */
    @Test
    void testCreateVaccinationRecord() throws IllegalOperationException {
        VaccinationRecordEntity newEntity = factory.manufacturePojo(VaccinationRecordEntity.class);
        newEntity.setVaccineName("Rabia");
        newEntity.setVaccineDate(LocalDate.now());
        newEntity.setNextDosesDate(LocalDate.now().plusMonths(1));

        VaccinationRecordEntity result = vaccinationRecordService.createVaccinationRecord(newEntity);
        assertNotNull(result);

        VaccinationRecordEntity stored = entityManager.find(VaccinationRecordEntity.class, result.getId());
        assertNotNull(stored);
        assertEquals(newEntity.getVaccineName(), stored.getVaccineName());
        assertEquals(newEntity.getVaccineDate(), stored.getVaccineDate());
        assertEquals(newEntity.getNextDosesDate(), stored.getNextDosesDate());
    }

    /**
     * Prueba para crear un VaccinationRecord con vaccineName nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateVaccinationRecordNullVaccineName() {
        assertThrows(IllegalOperationException.class, () -> {
            VaccinationRecordEntity newEntity = factory.manufacturePojo(VaccinationRecordEntity.class);
            newEntity.setVaccineName(null);
            newEntity.setVaccineDate(LocalDate.now());
            newEntity.setNextDosesDate(LocalDate.now().plusMonths(1));
            vaccinationRecordService.createVaccinationRecord(newEntity);
        });
    }

    /**
     * Prueba para crear un VaccinationRecord con vaccineName vacío.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateVaccinationRecordEmptyVaccineName() {
        assertThrows(IllegalOperationException.class, () -> {
            VaccinationRecordEntity newEntity = factory.manufacturePojo(VaccinationRecordEntity.class);
            newEntity.setVaccineName("");
            newEntity.setVaccineDate(LocalDate.now());
            newEntity.setNextDosesDate(LocalDate.now().plusMonths(1));
            vaccinationRecordService.createVaccinationRecord(newEntity);
        });
    }

    /**
     * Prueba para crear un VaccinationRecord con vaccineDate nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateVaccinationRecordNullVaccineDate() {
        assertThrows(IllegalOperationException.class, () -> {
            VaccinationRecordEntity newEntity = factory.manufacturePojo(VaccinationRecordEntity.class);
            newEntity.setVaccineName("Rabia");
            newEntity.setVaccineDate(null);
            newEntity.setNextDosesDate(LocalDate.now().plusMonths(1));
            vaccinationRecordService.createVaccinationRecord(newEntity);
        });
    }

    /**
     * Prueba para crear un VaccinationRecord con nextDosesDate nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateVaccinationRecordNullNextDosesDate() {
        assertThrows(IllegalOperationException.class, () -> {
            VaccinationRecordEntity newEntity = factory.manufacturePojo(VaccinationRecordEntity.class);
            newEntity.setVaccineName("Rabia");
            newEntity.setVaccineDate(LocalDate.now());
            newEntity.setNextDosesDate(null);
            vaccinationRecordService.createVaccinationRecord(newEntity);
        });
    }

  
    /**
     * Prueba para actualizar un VaccinationRecord existente con todos los campos.
     */
    @Test
    void testUpdateVaccinationRecord() throws EntityNotFoundException {
        VaccinationRecordEntity existing = vaccinationRecordList.get(0);

        VaccinationRecordEntity updatedData = factory.manufacturePojo(VaccinationRecordEntity.class);
        updatedData.setVaccineName("VacunaActualizada");
        updatedData.setVaccineDate(LocalDate.now());
        updatedData.setNextDosesDate(LocalDate.now().plusMonths(2));

        VaccinationRecordEntity result = vaccinationRecordService.update(existing.getId(), updatedData);
        assertNotNull(result);

        VaccinationRecordEntity stored = entityManager.find(VaccinationRecordEntity.class, existing.getId());
        assertEquals("VacunaActualizada", stored.getVaccineName());
        assertEquals(updatedData.getVaccineDate(), stored.getVaccineDate());
        assertEquals(updatedData.getNextDosesDate(), stored.getNextDosesDate());
    }

    /**
     * Prueba para actualizar solo el vaccineName.
     * Los campos nulos no deben modificar los valores originales.
     */
    @Test
    void testUpdateVaccinationRecordOnlyName() throws EntityNotFoundException {
        VaccinationRecordEntity existing = vaccinationRecordList.get(1);
        LocalDate originalVaccineDate = existing.getVaccineDate();
        LocalDate originalNextDosesDate = existing.getNextDosesDate();

        VaccinationRecordEntity partialUpdate = new VaccinationRecordEntity();
        partialUpdate.setVaccineName("SoloNombreActualizado");

        vaccinationRecordService.update(existing.getId(), partialUpdate);

        VaccinationRecordEntity stored = entityManager.find(VaccinationRecordEntity.class, existing.getId());
        assertEquals("SoloNombreActualizado", stored.getVaccineName());
        assertEquals(originalVaccineDate, stored.getVaccineDate());
        assertEquals(originalNextDosesDate, stored.getNextDosesDate());
    }

    /**
     * Prueba para actualizar un VaccinationRecord que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testUpdateInvalidVaccinationRecord() {
        assertThrows(EntityNotFoundException.class, () -> {
            VaccinationRecordEntity updatedData = factory.manufacturePojo(VaccinationRecordEntity.class);
            vaccinationRecordService.update(0L, updatedData);
        });
    }


    /**
     * Prueba para eliminar un VaccinationRecord existente.
     */
    @Test
    void testDeleteVaccinationRecord() throws Exception {
        VaccinationRecordEntity entity = vaccinationRecordList.get(0);
        vaccinationRecordService.deleteVaccinationRecord(entity.getId());

        VaccinationRecordEntity deleted = entityManager.find(VaccinationRecordEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un VaccinationRecord que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testDeleteInvalidVaccinationRecord() {
        assertThrows(EntityNotFoundException.class, () -> {
            vaccinationRecordService.deleteVaccinationRecord(0L);
        });
    }
}