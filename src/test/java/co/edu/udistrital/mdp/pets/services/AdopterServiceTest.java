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
        adopter.setClientName("Juan Perez");
        adopter.setClientPhone("123456789");
        adopter.setClientEmail("juan@mail.com");
        adopter.setHasChildren(true);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Juan Perez", saved.getClientName());
        assertEquals(true, saved.getHasChildren());
    }

    @Test
    void testGetAdopter() throws EntityNotFoundException {
        AdopterEntity adopter = new AdopterEntity();
        adopter.setClientName("Maria Lopez");
        adopter.setClientPhone("987654321");
        adopter.setClientEmail("maria@mail.com");
        adopter.setHasChildren(true);
        adopter.setHasPets(true);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        AdopterEntity found = adopterService.getAdopter(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Maria Lopez", found.getClientName());
        assertEquals(true, found.getHasPets());
    }

    @Test
    void testGetAdopters() {
        AdopterEntity a1 = new AdopterEntity();
        a1.setClientName("C1");
        a1.setClientPhone("111");
        a1.setClientEmail("c1@mail.com");
        a1.setHasChildren(true);
        a1.setHasPets(false);
        adopterService.createAdopter(a1);

        AdopterEntity a2 = new AdopterEntity();
        a2.setClientName("C2");
        a2.setClientPhone("222");
        a2.setClientEmail("c2@mail.com");
        a2.setHasChildren(false);
        a2.setHasPets(true);
        adopterService.createAdopter(a2);

        List<AdopterEntity> list = adopterService.getAdopters();

        assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdateAdopter() throws EntityNotFoundException {
        AdopterEntity adopter = new AdopterEntity();
        adopter.setClientName("Old Name");
        adopter.setClientPhone("000");
        adopter.setClientEmail("old@mail.com");
        adopter.setHasChildren(false);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        AdopterEntity updateData = new AdopterEntity();
        updateData.setClientName("New Name");
        updateData.setClientPhone("999");
        updateData.setClientEmail("new@mail.com");
        updateData.setHasChildren(true);
        updateData.setHasPets(true);

        AdopterEntity updated = adopterService.updateAdopter(saved.getId(), updateData);

        assertEquals("New Name", updated.getClientName());
        assertEquals("999", updated.getClientPhone());
        assertEquals("new@mail.com", updated.getClientEmail());
        assertEquals(true, updated.getHasChildren());
        assertEquals(true, updated.getHasPets());
    }

    @Test
    void testDeleteAdopter() throws EntityNotFoundException {
        AdopterEntity adopter = new AdopterEntity();
        adopter.setClientName("ToDelete");
        adopter.setClientPhone("000");
        adopter.setClientEmail("delete@mail.com");
        adopter.setHasChildren(false);
        adopter.setHasPets(false);

        AdopterEntity saved = adopterService.createAdopter(adopter);

        adopterService.deleteAdopter(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            adopterService.getAdopter(saved.getId());
        });
    }
}