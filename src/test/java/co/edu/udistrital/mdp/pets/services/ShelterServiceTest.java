package co.edu.udistrital.mdp.pets.services;
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
@Import(ShelterService.class)
@ContextConfiguration(classes = MainApplication.class)
public class ShelterServiceTest {

	@Autowired
	private ShelterService shelterService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private List<ShelterEntity> shelterList = new ArrayList<>();
	private ShelterEventEntity shelterEventEntity;
    
	//Configuracion inicial.
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
        //Crea 3 refugios.
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);

            //Verifica el formato del correo.
            shelter.setEmail("shelter" + i + "@example.com");

            entityManager.persist(shelter);
            shelterList.add(shelter);
        }
        //Crea un evento asociado a uno de los refugios.
        shelterEventEntity = factory.manufacturePojo(ShelterEventEntity.class);
        shelterEventEntity.setShelter(shelterList.get(0));

        //Persistencia para el evento.
        entityManager.persist(shelterEventEntity);

        //Agrega el evento.
        shelterList.get(0).getEvents().add(shelterEventEntity);
    }

    //Test para la creación de un refugio sin problemas.
    @Test
    void createShelter() throws EntityNotFoundException, IllegalOperationException {
        //Crea el refugio.
        ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);

        newShelter.setEmail("newemail@test.com");

        ShelterEntity result = shelterService.createShelter(newShelter);
        //Invoca ShelterService.
        assertNotNull(result);

		ShelterEntity entity = entityManager.find(ShelterEntity.class, result.getId());

		assertEquals(newShelter.getName(), entity.getName());
		assertEquals(newShelter.getCity(), entity.getCity());
		assertEquals(newShelter.getAddress(), entity.getAddress());
		assertEquals(newShelter.getEmail(), entity.getEmail());

    }

    //Test para nombre repetido.
	@Test
	void testCreateShelterWithExistingName() {

		assertThrows(IllegalOperationException.class, () -> {
			ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);
			//mismo nombre
			newShelter.setName(shelterList.get(0).getName());
			newShelter.setEmail("uniqueemail@test.com");
			shelterService.createShelter(newShelter);
		});
	}

    //Test para email repetido.
    @Test
    void createShelterRepeatedEmail() {
        assertThrows(IllegalOperationException.class, ()->{
            //Crea refugio.
            ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);
            //mismo email
            newShelter.setEmail(shelterList.get(0).getEmail());
            shelterService.createShelter(newShelter);
        });
    }

    //Test para update de refugio.
    @Test
    void updateShelter() throws EntityNotFoundException, IllegalOperationException {
        //Obtiene un refugio.
        ShelterEntity entity = shelterList.get(0);
        ShelterEntity pojoEntity = factory.manufacturePojo(ShelterEntity.class);
        //Asigna el id existente.
        pojoEntity.setId(entity.getId());
        pojoEntity.setEmail("updated@email.com");
        shelterService.updateShelter(entity.getId(), pojoEntity);

        //Busca el refugio y actualiza los datos.
        ShelterEntity resp = entityManager.find(ShelterEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(),resp.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getCity(), resp.getCity());
        assertEquals(pojoEntity.getAddress(), resp.getAddress());
        assertEquals(pojoEntity.getEmail(), resp.getEmail());
    }

    //Test para update con nombre repetido.
    @Test
    void updateShelterWithExistingName() {
        assertThrows(IllegalOperationException.class, () -> {

            ShelterEntity entity = shelterList.get(1);

            ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);
            newShelter.setName(shelterList.get(0).getName());
            newShelter.setEmail("unique@test.com");

            shelterService.updateShelter(entity.getId(), newShelter);
        });
    }

    //Test para update con email repetido.
    @Test
    void updateShelterWithExistingEmail() {
        assertThrows(IllegalOperationException.class, () -> {

            ShelterEntity entity = shelterList.get(1);

            ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);
            newShelter.setEmail(shelterList.get(0).getEmail());

            shelterService.updateShelter(entity.getId(), newShelter);
        });
    }

    //Test para update de refugio inexistente.
    @Test
    void updateNonExistingShelter() {
        assertThrows(EntityNotFoundException.class, () -> {
            ShelterEntity newShelter = factory.manufacturePojo(ShelterEntity.class);
            shelterService.updateShelter(999L, newShelter);
        });
    }

    //Test para delete de refugio.
	@Test
	void testDeleteShelter() throws EntityNotFoundException, IllegalOperationException {

		ShelterEntity entity = shelterList.get(1);

		shelterService.deleteShelter(entity.getId());

		ShelterEntity deleted = entityManager.find(ShelterEntity.class, entity.getId());

		assertNull(deleted);
	}

    //Test para delete de refugio con eventos asociados.
    @Test
    void deleteShelterWithAssociatedEvents() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEntity entity = shelterList.get(0);
            shelterService.deleteShelter(entity.getId());
        });
    }

    //Test para delete de refugio inexistente.
    @Test
    void deleteNonExistingShelter() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterService.deleteShelter(999L);
        });
    }

}
