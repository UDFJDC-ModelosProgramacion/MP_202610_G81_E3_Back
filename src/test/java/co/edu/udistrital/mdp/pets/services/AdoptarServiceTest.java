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

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;

@DataJpaTest
@Import(AdoptarService.class)
class AdoptarServiceTest {

    @Autowired
    private AdoptarService adoptarService;

    @Autowired
    private TestEntityManager entityManager;

    private List<AdopterEntity> adopters = new ArrayList<>();
    private List<PetEntity> pets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager()
                .createQuery("delete from AdoptionEntity")
                .executeUpdate();

        entityManager.getEntityManager()
                .createQuery("delete from PetEntity")
                .executeUpdate();

        entityManager.getEntityManager()
                .createQuery("delete from AdopterEntity")
                .executeUpdate();
    }

    private void insertData() {

        for (int i = 0; i < 2; i++) {

            AdopterEntity adopter = new AdopterEntity();
            entityManager.persist(adopter);
            adopters.add(adopter);

            PetEntity pet = new PetEntity();
            // QUITADO: pet.setStatus("AVAILABLE");
            entityManager.persist(pet);
            pets.add(pet);
        }
    }

    @Test
    void testAdopt() {

        // ARRANGE
        AdopterEntity adopter = adopters.get(0);
        PetEntity pet = pets.get(0);

        // ACT
        adoptarService.adopt(adopter.getId(), pet.getId());

        // ASSERT
        AdoptionEntity adoption = entityManager
                .getEntityManager()
                .createQuery("SELECT a FROM AdoptionEntity a", AdoptionEntity.class)
                .getSingleResult();

        assertNotNull(adoption);
        assertEquals(adopter.getId(), adoption.getAdopter().getId());
        assertEquals(pet.getId(), adoption.getPet().getId());
    }
}