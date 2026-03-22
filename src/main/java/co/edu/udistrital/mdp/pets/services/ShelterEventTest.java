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

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterEventService.class)
class ShelterEventServiceTest {

    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ShelterEventEntity> eventList = new ArrayList<>();
    private ShelterEntity shelterEntity;

    //Configuración inicial.
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    //Limpia las tablas.
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    //Inserta datos de prueba.
    private void insertData() {

        //Crea un refugio.
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);

        //Crea 3 eventos asociados al refugio.
        for (int i = 0; i < 3; i++) {
            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);

            //Asignar refugio al evento.
            event.setShelter(shelterEntity);
            //Asignar fecha distinta para evitar conflicto.
            event.setDate(LocalDate.now().plusDays(i));
            entityManager.persist(event);
            eventList.add(event);
        }
    }

    //Test para crear un evento correctamente.
    @Test
    void createShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        //Crear evento.
        ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newEvent.setShelter(shelterEntity);
        newEvent.setDate(LocalDate.now().plusDays(10));
        ShelterEventEntity result = shelterEventService.createShelterEvent(newEvent);
        //Verificar que se creó.
        assertNotNull(result);
        ShelterEventEntity entity = entityManager.find(ShelterEventEntity.class, result.getId());
        assertEquals(newEvent.getName(), entity.getName());
        assertEquals(newEvent.getDate(), entity.getDate());
        assertEquals(newEvent.getDescription(), entity.getDescription());
    }

    //Test para crear evento con fecha repetida.
    @Test
    void createShelterEventWithRepeatedDate() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
            //Usa fecha ya existente.
            newEvent.setDate(eventList.get(0).getDate());
            newEvent.setShelter(shelterEntity);
            shelterEventService.createShelterEvent(newEvent);
        });
    }

    //Test para actualizar un evento.
    @Test
    void updateShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        //Obtiene evento existente.
        ShelterEventEntity entity = eventList.get(0);
        ShelterEventEntity pojoEntity = factory.manufacturePojo(ShelterEventEntity.class);

        pojoEntity.setShelter(shelterEntity);
        pojoEntity.setDate(LocalDate.now().plusDays(20));
        shelterEventService.updateShelterEventEntity(entity.getId(), pojoEntity);
        //Busca evento actualizado.
        ShelterEventEntity resp = entityManager.find(ShelterEventEntity.class, entity.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getDate(), resp.getDate());
        assertEquals(pojoEntity.getDescription(), resp.getDescription());
    }

    //Test para actualizar evento inexistente.
    @Test
    void updateNonExistingShelterEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
            newEvent.setShelter(shelterEntity);
            newEvent.setDate(LocalDate.now().plusDays(5));
            shelterEventService.updateShelterEventEntity(999L, newEvent);
        });
    }

    //Test para eliminar un evento.
    @Test
    void deleteShelterEvent() throws EntityNotFoundException, IllegalOperationException {
        //Obtiene evento existente.
        ShelterEventEntity entity = eventList.get(1);
        shelterEventService.deleteShelterEvent(entity.getId());
        ShelterEventEntity deleted = entityManager.find(ShelterEventEntity.class, entity.getId());
        //Verificar que fue eliminado.
        assertNull(deleted);
    }

    //Test para eliminar evento inexistente.
    @Test
    void deleteNonExistingShelterEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterEventService.deleteShelterEvent(999L);
        });
    }
}