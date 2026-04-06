package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
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

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionStatus;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionService.class)
class AdoptionServiceTest {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionEntity> adoptionList = new ArrayList<>();

    private AdopterEntity adopter;
    private PetEntity pet;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(pet);

        for (int i = 0; i < 3; i++) {

            // 🔥 CORRECCIÓN AQUÍ
            AdoptionEntity adoption = new AdoptionEntity();

            adoption.setAdopter(adopter);
            adoption.setPet(pet);
            adoption.setAdoptionDate(LocalDate.now());
            adoption.setTrialStartDate(LocalDate.now());
            adoption.setStatus(AdoptionStatus.IN_TRIAL);

            // 🔥 EVITA EL ERROR DE HIBERNATE
            adoption.setFollowUp(null);
            adoption.setReturnPet(null);

            entityManager.persist(adoption);
            adoptionList.add(adoption);
        }

        // 🔥 IMPORTANTE PARA SINCRONIZAR
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateAdoption() throws EntityNotFoundException {
        AdoptionEntity newAdoption = new AdoptionEntity();

        newAdoption.setAdopter(adopter);
        newAdoption.setPet(pet);
        newAdoption.setFollowUp(null);
        newAdoption.setReturnPet(null);

        AdoptionEntity result = adoptionService.createAdoption(
                adopter.getId(),
                pet.getId(),
                newAdoption
        );

        assertNotNull(result);
        assertEquals(adopter.getId(), result.getAdopter().getId());
        assertEquals(pet.getId(), result.getPet().getId());
    }

    @Test
    void testUpdateAdoption() throws EntityNotFoundException {
        AdoptionEntity entity = adoptionList.get(0);

        AdoptionEntity updatedData = new AdoptionEntity();
        updatedData.setStatus(AdoptionStatus.COMPLETED);
        updatedData.setTrialEndDate(LocalDate.now().plusDays(30));

        AdoptionEntity updated = adoptionService.updateAdoption(entity.getId(), updatedData);

        assertEquals(AdoptionStatus.COMPLETED, updated.getStatus());
        assertEquals(updatedData.getTrialEndDate(), updated.getTrialEndDate());
    }

    @Test
    void testGetAdoptionById() throws EntityNotFoundException {
        AdoptionEntity entity = adoptionList.get(1);
        AdoptionEntity found = adoptionService.getAdoption(entity.getId());

        assertEquals(entity.getId(), found.getId());
        assertEquals(entity.getStatus(), found.getStatus());
    }

    @Test
    void testGetAllAdoptions() {
        List<AdoptionEntity> all = adoptionService.getAdoptions();
        assertEquals(adoptionList.size(), all.size());
    }
}