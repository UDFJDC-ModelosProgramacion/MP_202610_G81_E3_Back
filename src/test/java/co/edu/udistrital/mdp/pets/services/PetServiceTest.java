package co.edu.udistrital.mdp.pets.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PetService.class)
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);
        }
    }

    //Prueba añadida (puede ser cambiada posteriormente).
    @Test
    void testCreatePet() throws EntityNotFoundException, IllegalOperationException {
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        // Se prueba con datos específicos para asegurar que se guardan en la base de datos.
        newPet.setName("Bingo");
        newPet.setSpecies("Perro");
        newPet.setBreed("Labrador");
        newPet.setSex("Macho");
        newPet.setSize(50.0f);
        newPet.setArriveToShelter(new java.sql.Date(System.currentTimeMillis()));
        newPet.setSpecificRequirements("Ninguno");

        newPet.setPhotographes(new ArrayList<>());
        newPet.getPhotographes().add(new MediaFileEntity());

        PetEntity result = petService.createPet(newPet);

        assertNotNull(result);

        PetEntity entity = entityManager.find(PetEntity.class, result.getId());
        assertEquals(newPet.getName(), entity.getName());
        assertEquals(newPet.getSpecies(), entity.getSpecies());
        assertEquals(newPet.getBreed(), entity.getBreed());
    }
}