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
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnPetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReturnPetService.class)
@ContextConfiguration(classes = MainApplication.class)
class ReturnPetServiceTest {

    @Autowired
    private ReturnPetService returnPetService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<ReturnPetEntity> returnList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReturnPetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            // Crear y persistir Adopter.
            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);
            // Crear y persistir Pet.
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            // Crear y persistir FollowUp.
            FollowUpEntity followUp = factory.manufacturePojo(FollowUpEntity.class);
            entityManager.persist(followUp);
            // Crear Adoption.
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setAdopter(adopter);
            adoption.setPet(pet);
            adoption.setFollowUp(followUp);
            entityManager.persist(adoption);
            // Crear ReturnPet.
            ReturnPetEntity returnPetEntity = factory.manufacturePojo(ReturnPetEntity.class);
            returnPetEntity.setReason("Razón de devolución " + i);
            returnPetEntity.setReturnDate(LocalDate.now().minusDays(i + 1));
            returnPetEntity.setAdoption(adoption);

            //Sincronizar la relación.
            adoption.setReturnPet(returnPetEntity);

            entityManager.persist(returnPetEntity);
            entityManager.persist(adoption);
            returnList.add(returnPetEntity);
        }
    }
    // Tests: createReturn

    @Test
    void testCreateReturnValid() throws IllegalOperationException {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(pet);

        FollowUpEntity followUp = factory.manufacturePojo(FollowUpEntity.class);
        entityManager.persist(followUp);

        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setAdopter(adopter);
        adoption.setPet(pet);
        adoption.setFollowUp(followUp);
        entityManager.persist(adoption);

        ReturnPetEntity newReturn = factory.manufacturePojo(ReturnPetEntity.class);
        newReturn.setReason("Problemas de salud del adoptante");
        newReturn.setReturnDate(LocalDate.now());
        newReturn.setAdoption(adoption);

        //Sincronizar relación.
        adoption.setReturnPet(newReturn);

        ReturnPetEntity result = returnPetService.createReturn(newReturn);

        assertNotNull(result);
        ReturnPetEntity entity = entityManager.find(ReturnPetEntity.class, result.getId());
        assertEquals(newReturn.getReason(), entity.getReason());
        assertEquals(newReturn.getReturnDate(), entity.getReturnDate());
    }

    @Test
    void testCreateReturnReasonEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnPetEntity newReturn = factory.manufacturePojo(ReturnPetEntity.class);
            newReturn.setReason("   ");
            newReturn.setReturnDate(LocalDate.now());
            returnPetService.createReturn(newReturn);
        });
    }

    @Test
    void testCreateReturnDateInFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnPetEntity newReturn = factory.manufacturePojo(ReturnPetEntity.class);
            newReturn.setReason("Razón válida");
            newReturn.setReturnDate(LocalDate.now().plusDays(5));
            returnPetService.createReturn(newReturn);
        });
    }

    // Tests: getReturns

    @Test
    void testGetReturns() {
        List<ReturnPetEntity> returns = returnPetService.getReturns();
        assertFalse(returns.isEmpty());
        assertEquals(3, returns.size());
    }

    // Tests: getReturn

    @Test
    void testGetReturnValid() throws EntityNotFoundException {
        ReturnPetEntity entity = returnList.get(0);
        ReturnPetEntity found = returnPetService.getReturn(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getId(), found.getId());
        assertEquals(entity.getReason(), found.getReason());
    }

    @Test
    void testGetReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> returnPetService.getReturn(0L));
    }

    // Tests: updateReturn

    @Test
    void testUpdateReturnValid() throws EntityNotFoundException, IllegalOperationException {
        ReturnPetEntity entity = returnList.get(0);
        ReturnPetEntity pojoEntity = factory.manufacturePojo(ReturnPetEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setReason("Nueva razón de devolución");
        pojoEntity.setReturnDate(LocalDate.now().minusDays(2));

        returnPetService.updateReturn(entity.getId(), pojoEntity);

        ReturnPetEntity resp = entityManager.find(ReturnPetEntity.class, entity.getId());
        assertEquals(pojoEntity.getReason(), resp.getReason());
        assertEquals(pojoEntity.getReturnDate(), resp.getReturnDate());
    }

    @Test
    void testUpdateReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            ReturnPetEntity pojoEntity = factory.manufacturePojo(ReturnPetEntity.class);
            pojoEntity.setReason("Razón válida");
            pojoEntity.setReturnDate(LocalDate.now());
            returnPetService.updateReturn(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateReturnReasonNull() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnPetEntity entity = returnList.get(0);
            ReturnPetEntity pojoEntity = factory.manufacturePojo(ReturnPetEntity.class);
            pojoEntity.setReason(null);
            pojoEntity.setReturnDate(LocalDate.now());
            returnPetService.updateReturn(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testUpdateReturnDateInFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnPetEntity entity = returnList.get(0);
            ReturnPetEntity pojoEntity = factory.manufacturePojo(ReturnPetEntity.class);
            pojoEntity.setReason("Razón válida");
            pojoEntity.setReturnDate(LocalDate.now().plusDays(3));
            returnPetService.updateReturn(entity.getId(), pojoEntity);
        });
    }

    // Tests: deleteReturn

    @Test
    void testDeleteReturnValid() throws EntityNotFoundException {
        ReturnPetEntity entity = returnList.get(1);
        returnPetService.deleteReturn(entity.getId());
        ReturnPetEntity deleted = entityManager.find(ReturnPetEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testCreateReturnReasonNull() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnPetEntity newReturn = factory.manufacturePojo(ReturnPetEntity.class);
            newReturn.setReason(null);
            newReturn.setReturnDate(LocalDate.now());
            returnPetService.createReturn(newReturn);
        });
    }


    @Test
    void testDeleteReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> returnPetService.deleteReturn(0L));
    }
}