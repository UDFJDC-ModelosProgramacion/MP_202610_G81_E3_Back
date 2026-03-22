import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VeterinaryRepository;
import co.edu.udistrital.mdp.pets.services.VeterinaryService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import(VeterinaryService.class)
public class VeterinaryServiceTest {

    @Autowired
    private VeterinaryService VeterinaryService;

    @Autowired
    private VeterinaryRepository veterinaryRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private VeterinaryEntity veterinaryEntity;

    @BeforeEach
    void setUp() throws IllegalOperationException {
        veterinaryRepository.deleteAll();

        veterinaryEntity = factory.manufacturePojo(VeterinaryEntity.class);
        veterinaryEntity.setId(null);
        veterinaryEntity.setName("Dr. Carlos Pérez");
        veterinaryEntity.setEmail("carlos.perez@veterinaria.com");
        veterinaryEntity.setSpecialty("Medicina general");
        veterinaryEntity.setAvailability("Lunes a Viernes");
        veterinaryEntity.setShelter(null);
        veterinaryEntity.setFollowUps(new java.util.ArrayList<>());
        veterinaryEntity = VeterinaryService.createVeterinary(veterinaryEntity);
    }

    // Tests: createVeterinary

    @Test
    void testCreateVeterinaryValid() throws IllegalOperationException {
        VeterinaryEntity newVet = new VeterinaryEntity();
        newVet.setName("Dra. Ana García");
        newVet.setEmail("ana.garcia@veterinaria.com");
        newVet.setSpecialty("Cirugía");

        VeterinaryEntity result = VeterinaryService.createVeterinary(newVet);

        assertNotNull(result.getId());
        assertEquals("Dra. Ana García", result.getName());
    }

    @Test
    void testCreateVeterinaryNameNull() {
        VeterinaryEntity newVet = new VeterinaryEntity();
        newVet.setName(null);
        newVet.setEmail("test@test.com");
        newVet.setSpecialty("Cirugía");

        assertThrows(IllegalOperationException.class, () -> VeterinaryService.createVeterinary(newVet));
    }

    @Test
    void testCreateVeterinaryNameEmpty() {
        VeterinaryEntity newVet = new VeterinaryEntity();
        newVet.setName("   ");
        newVet.setEmail("test@test.com");
        newVet.setSpecialty("Cirugía");

        assertThrows(IllegalOperationException.class, () -> VeterinaryService.createVeterinary(newVet));
    }

    @Test
    void testCreateVeterinaryEmailNull() {
        VeterinaryEntity newVet = new VeterinaryEntity();
        newVet.setName("Dr. Test");
        newVet.setEmail(null);
        newVet.setSpecialty("Cirugía");

        assertThrows(IllegalOperationException.class, () -> VeterinaryService.createVeterinary(newVet));
    }

    @Test
    void testCreateVeterinarySpecialtyNull() {
        VeterinaryEntity newVet = new VeterinaryEntity();
        newVet.setName("Dr. Test");
        newVet.setEmail("test@test.com");
        newVet.setSpecialty(null);

        assertThrows(IllegalOperationException.class, () -> VeterinaryService.createVeterinary(newVet));
    }

    // Tests: getVeterinaries

    @Test
    void testGetVeterinaries() {
        List<VeterinaryEntity> vets = VeterinaryService.getVeterinaries();
        assertFalse(vets.isEmpty());
    }

    // Tests: getVeterinary

    @Test
    void testGetVeterinaryValid() throws EntityNotFoundException {
        VeterinaryEntity found = VeterinaryService.getVeterinary(veterinaryEntity.getId());
        assertNotNull(found);
        assertEquals(veterinaryEntity.getId(), found.getId());
    }

    @Test
    void testGetVeterinaryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> VeterinaryService.getVeterinary(0L));
    }

    // Tests: updateVeterinary

    @Test
    void testUpdateVeterinaryValid() throws EntityNotFoundException, IllegalOperationException {
        VeterinaryEntity updated = new VeterinaryEntity();
        updated.setName("Dr. Luis Martínez");
        updated.setEmail("luis.martinez@vet.com");
        updated.setSpecialty("Dermatología");

        VeterinaryEntity result = VeterinaryService.updateVeterinary(veterinaryEntity.getId(), updated);

        assertEquals("Dr. Luis Martínez", result.getName());
        assertEquals("Dermatología", result.getSpecialty());
    }

    @Test
    void testUpdateVeterinaryNotFound() {
        VeterinaryEntity updated = new VeterinaryEntity();
        updated.setName("Dr. Test");
        updated.setEmail("test@test.com");
        updated.setSpecialty("Cirugía");

        assertThrows(EntityNotFoundException.class, () -> VeterinaryService.updateVeterinary(0L, updated));
    }

    @Test
    void testUpdateVeterinaryNameNull() {
        VeterinaryEntity updated = new VeterinaryEntity();
        updated.setName(null);
        updated.setEmail("test@test.com");
        updated.setSpecialty("Cirugía");

        assertThrows(IllegalOperationException.class,
                () -> VeterinaryService.updateVeterinary(veterinaryEntity.getId(), updated));
    }

    @Test
    void testUpdateVeterinaryEmailEmpty() {
        VeterinaryEntity updated = new VeterinaryEntity();
        updated.setName("Dr. Test");
        updated.setEmail("   ");
        updated.setSpecialty("Cirugía");

        assertThrows(IllegalOperationException.class,
                () -> VeterinaryService.updateVeterinary(veterinaryEntity.getId(), updated));
    }

    @Test
    void testUpdateVeterinarySpecialtyNull() {
        VeterinaryEntity updated = new VeterinaryEntity();
        updated.setName("Dr. Test");
        updated.setEmail("test@test.com");
        updated.setSpecialty(null);

        assertThrows(IllegalOperationException.class,
                () -> VeterinaryService.updateVeterinary(veterinaryEntity.getId(), updated));
    }

    // Tests: deleteVeterinary

    @Test
    void testDeleteVeterinaryValid() throws EntityNotFoundException {
        VeterinaryService.deleteVeterinary(veterinaryEntity.getId());
        assertThrows(EntityNotFoundException.class, () -> VeterinaryService.getVeterinary(veterinaryEntity.getId()));
    }

    @Test
    void testDeleteVeterinaryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> VeterinaryService.deleteVeterinary(0L));
    }
}