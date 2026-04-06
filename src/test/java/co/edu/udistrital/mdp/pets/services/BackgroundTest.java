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

import co.edu.udistrital.mdp.pets.entities.BackgroundEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(BackgroundService.class)
class BackgroundTest {

    @Autowired
    private BackgroundService backgroundService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<BackgroundEntity> backgroundList = new ArrayList<>();

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
                .createQuery("delete from BackgroundEntity")
                .executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BackgroundEntity entity = factory.manufacturePojo(BackgroundEntity.class);
            entity.setDescription("Descripcion_" + i);
            entity.setDate(LocalDate.now());
            entityManager.persist(entity);
            backgroundList.add(entity);
        }
    }

    /**
     * Prueba para crear un Background con datos válidos.
     */
    @Test
    void testCreateBackground() throws IllegalOperationException, EntityNotFoundException {
        BackgroundEntity newEntity = factory.manufacturePojo(BackgroundEntity.class);
        newEntity.setDescription("Alergia a medicamentos");
        newEntity.setDate(LocalDate.now());

        BackgroundEntity result = backgroundService.createBackground(newEntity);
        assertNotNull(result);

        BackgroundEntity stored = entityManager.find(BackgroundEntity.class, result.getId());
        assertNotNull(stored);
        assertEquals(newEntity.getDescription(), stored.getDescription());
        assertEquals(newEntity.getDate(), stored.getDate());
    }

    /**
     * Prueba para crear un Background con description nula.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateBackgroundNullDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            BackgroundEntity newEntity = factory.manufacturePojo(BackgroundEntity.class);
            newEntity.setDescription(null);
            newEntity.setDate(LocalDate.now());
            backgroundService.createBackground(newEntity);
        });
    }

    /**
     * Prueba para crear un Background con description vacía.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateBackgroundEmptyDescription() {
        assertThrows(IllegalOperationException.class, () -> {
            BackgroundEntity newEntity = factory.manufacturePojo(BackgroundEntity.class);
            newEntity.setDescription("");
            newEntity.setDate(LocalDate.now());
            backgroundService.createBackground(newEntity);
        });
    }

    /**
     * Prueba para crear un Background con date nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateBackgroundNullDate() {
        assertThrows(IllegalOperationException.class, () -> {
            BackgroundEntity newEntity = factory.manufacturePojo(BackgroundEntity.class);
            newEntity.setDescription("Alergia a medicamentos");
            newEntity.setDate(null);
            backgroundService.createBackground(newEntity);
        });
    }

    /**
     * Prueba para actualizar un Background existente con todos los campos.
     */
    @Test
    void testUpdateBackground() throws EntityNotFoundException {
        BackgroundEntity existing = backgroundList.get(0);

        BackgroundEntity updatedData = factory.manufacturePojo(BackgroundEntity.class);
        updatedData.setDescription("Descripcion actualizada");
        updatedData.setDate(LocalDate.now().plusDays(5));

        BackgroundEntity result = backgroundService.updateBackground(existing.getId(), updatedData);
        assertNotNull(result);

        BackgroundEntity stored = entityManager.find(BackgroundEntity.class, existing.getId());
        assertEquals("Descripcion actualizada", stored.getDescription());
        assertEquals(updatedData.getDate(), stored.getDate());
    }

    /**
     * Prueba para actualizar solo la description.
     * Los campos nulos no deben modificar los valores originales.
     */
    @Test
    void testUpdateBackgroundOnlyDescription() throws EntityNotFoundException {
        BackgroundEntity existing = backgroundList.get(1);
        LocalDate originalDate = existing.getDate();

        BackgroundEntity partialUpdate = new BackgroundEntity();
        partialUpdate.setDescription("Solo descripcion actualizada");

        backgroundService.updateBackground(existing.getId(), partialUpdate);

        BackgroundEntity stored = entityManager.find(BackgroundEntity.class, existing.getId());
        assertEquals("Solo descripcion actualizada", stored.getDescription());
        assertEquals(originalDate, stored.getDate());
    }

    /**
     * Prueba para actualizar un Background que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testUpdateInvalidBackground() {
        assertThrows(EntityNotFoundException.class, () -> {
            BackgroundEntity updatedData = factory.manufacturePojo(BackgroundEntity.class);
            backgroundService.updateBackground(0L, updatedData);
        });
    }

    /**
     * Prueba para eliminar un Background existente.
     */
    @Test
    void testDeleteBackground() throws Exception {
        BackgroundEntity entity = backgroundList.get(0);
        backgroundService.deleteBackground(entity.getId());

        BackgroundEntity deleted = entityManager.find(BackgroundEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un Background que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testDeleteInvalidBackground() {
        assertThrows(EntityNotFoundException.class, () -> {
            backgroundService.deleteBackground(0L);
        });
    }
}