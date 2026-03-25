package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VeterinaryService.class)
@ContextConfiguration(classes = MainApplication.class)
class VeterinaryServiceTest {

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<VeterinaryEntity> veterinaryList = new ArrayList<>();
    private FollowUpEntity followUpEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from FollowUpEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinaryEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VeterinaryEntity vet = factory.manufacturePojo(VeterinaryEntity.class);
            vet.setName("Dr. Veterinario " + i);
            vet.setEmail("vet" + i + "@example.com");
            vet.setSpecialty("Especialidad " + i);
            vet.setFollowUps(new ArrayList<>());
            vet.setShelter(null);
            entityManager.persist(vet);
            veterinaryList.add(vet);
        }

        followUpEntity = factory.manufacturePojo(FollowUpEntity.class);
        followUpEntity.setVeterinary(veterinaryList.get(0));
        followUpEntity.setPet(null);
        entityManager.persist(followUpEntity);
        veterinaryList.get(0).getFollowUps().add(followUpEntity);
    }

    // Tests: createVeterinary

    @Test
    void testCreateVeterinaryValid() throws IllegalOperationException {
        VeterinaryEntity newVet = factory.manufacturePojo(VeterinaryEntity.class);
        newVet.setName("Dra. Ana García");
        newVet.setEmail("ana.garcia@vet.com");
        newVet.setSpecialty("Cirugía");
        newVet.setFollowUps(new ArrayList<>());
        newVet.setShelter(null);

        VeterinaryEntity result = veterinaryService.createVeterinary(newVet);

        assertNotNull(result);
        VeterinaryEntity entity = entityManager.find(VeterinaryEntity.class, result.getId());
        assertEquals(newVet.getName(), entity.getName());
        assertEquals(newVet.getEmail(), entity.getEmail());
        assertEquals(newVet.getSpecialty(), entity.getSpecialty());
    }

    static Stream<Arguments> invalidVeterinaryProvider() {
        return Stream.of(
                Arguments.of(null, "test@test.com", "Cirugía"),
                Arguments.of("   ", "test@test.com", "Cirugía"),
                Arguments.of("Dr. Test", null, "Cirugía"),
                Arguments.of("Dr. Test", "test@test.com", null));
    }

    @ParameterizedTest
    @MethodSource("invalidVeterinaryProvider")
    void testCreateVeterinaryInvalid(String name, String email, String specialty) {
        assertThrows(IllegalOperationException.class, () -> {
            VeterinaryEntity newVet = new VeterinaryEntity();
            newVet.setName(name);
            newVet.setEmail(email);
            newVet.setSpecialty(specialty);
            veterinaryService.createVeterinary(newVet);
        });
    }

    // Tests: getVeterinaries

    @Test
    void testGetVeterinaries() {
        List<VeterinaryEntity> vets = veterinaryService.getVeterinaries();
        assertFalse(vets.isEmpty());
        assertEquals(3, vets.size());
    }

    // Tests: getVeterinary

    @Test
    void testGetVeterinaryValid() throws EntityNotFoundException {
        VeterinaryEntity entity = veterinaryList.get(0);
        VeterinaryEntity found = veterinaryService.getVeterinary(entity.getId());
        assertNotNull(found);
        assertEquals(entity.getId(), found.getId());
        assertEquals(entity.getName(), found.getName());
    }

    @Test
    void testGetVeterinaryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> veterinaryService.getVeterinary(0L));
    }

    // Tests: updateVeterinary

    @Test
    void testUpdateVeterinaryValid() throws EntityNotFoundException, IllegalOperationException {
        VeterinaryEntity entity = veterinaryList.get(0);
        VeterinaryEntity pojoEntity = factory.manufacturePojo(VeterinaryEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setName("Dr. Luis Martínez");
        pojoEntity.setEmail("luis.martinez@vet.com");
        pojoEntity.setSpecialty("Dermatología");
        pojoEntity.setFollowUps(new ArrayList<>());
        pojoEntity.setShelter(null);

        veterinaryService.updateVeterinary(entity.getId(), pojoEntity);

        VeterinaryEntity resp = entityManager.find(VeterinaryEntity.class, entity.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getEmail(), resp.getEmail());
        assertEquals(pojoEntity.getSpecialty(), resp.getSpecialty());
    }

    @Test
    void testUpdateVeterinaryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            VeterinaryEntity pojoEntity = factory.manufacturePojo(VeterinaryEntity.class);
            pojoEntity.setName("Dr. Test");
            pojoEntity.setEmail("test@test.com");
            pojoEntity.setSpecialty("Cirugía");
            veterinaryService.updateVeterinary(0L, pojoEntity);
        });
    }

    static Stream<Arguments> invalidUpdateVeterinaryProvider() {
        return Stream.of(
                Arguments.of(null, "test@test.com", "Cirugía"),
                Arguments.of("Dr. Test", "   ", "Cirugía"),
                Arguments.of("Dr. Test", "test@test.com", null));
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateVeterinaryProvider")
    void testUpdateVeterinaryInvalid(String name, String email, String specialty) {
        assertThrows(IllegalOperationException.class, () -> {
            VeterinaryEntity entity = veterinaryList.get(0);
            VeterinaryEntity pojoEntity = factory.manufacturePojo(VeterinaryEntity.class);
            pojoEntity.setName(name);
            pojoEntity.setEmail(email);
            pojoEntity.setSpecialty(specialty);
            veterinaryService.updateVeterinary(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteVeterinaryValid() throws EntityNotFoundException {
        VeterinaryEntity entity = veterinaryList.get(1);
        veterinaryService.deleteVeterinary(entity.getId());
        VeterinaryEntity deleted = entityManager.find(VeterinaryEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteVeterinaryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> veterinaryService.deleteVeterinary(0L));
    }
}