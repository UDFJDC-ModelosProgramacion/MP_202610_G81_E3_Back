package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

@SpringBootTest
@Transactional
public class AdopterServiceTest {

    @Autowired
    private AdopterService adopterService;

    @Test
    void testCreateAdopter() {

        AdopterEntity adopter = new AdopterEntity();
        adopter.setHasChildren(true);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(true, saved.getHasChildren());
    }

    @Test
    void testGetAdopter() throws EntityNotFoundException {

        AdopterEntity adopter = new AdopterEntity();
        adopter.setHasChildren(true);
        adopter.setHasPets(true);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        AdopterEntity found = adopterService.getAdopter(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals(true, found.getHasPets());
    }

    @Test
    void testGetAdopters() {

        AdopterEntity a1 = new AdopterEntity();
        a1.setHasChildren(true);
        a1.setHasPets(false);
        adopterService.createAdopter(a1);

        AdopterEntity a2 = new AdopterEntity();
        a2.setHasChildren(false);
        a2.setHasPets(true);
        adopterService.createAdopter(a2);

        List<AdopterEntity> list = adopterService.getAdopters();

        assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdateAdopter() throws EntityNotFoundException {

        AdopterEntity adopter = new AdopterEntity();
        adopter.setHasChildren(false);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        AdopterEntity updateData = new AdopterEntity();
        updateData.setHasChildren(true);
        updateData.setHasPets(true);

        AdopterEntity updated = adopterService.updateAdopter(saved.getId(), updateData);

        assertEquals(true, updated.getHasChildren());
        assertEquals(true, updated.getHasPets());
    }

    @Test
    void testDeleteAdopter() throws EntityNotFoundException {

        AdopterEntity adopter = new AdopterEntity();
        adopter.setHasChildren(false);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        adopterService.deleteAdopter(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            adopterService.getAdopter(saved.getId());
        });
    }
}