package co.edu.udistrital.mdp.pets.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PetMediaFileService.class)
class PetMediaFileTest {

    @Autowired
    private PetMediaFileService petMediaFileService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PetEntity> petList = new ArrayList<>();
    private List<MediaFileEntity> mediaFileList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager()
                .createQuery("delete from MediaFileEntity")
                .executeUpdate();
        entityManager.getEntityManager()
                .createQuery("delete from PetEntity")
                .executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        // Crear mascotas
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setPhotographs(new ArrayList<>());
            entityManager.persist(pet);
            petList.add(pet);
        }

        // Crear archivos y asociarlos a la primera mascota
        for (int i = 0; i < 3; i++) {
            MediaFileEntity mediaFile = factory.manufacturePojo(MediaFileEntity.class);
            mediaFile.setUrl("https://example.com/file_" + i + ".jpg");
            mediaFile.setPet(petList.get(0));
            mediaFile.setShelter(null);
            entityManager.persist(mediaFile);
            mediaFileList.add(mediaFile);
            petList.get(0).getPhotographs().add(mediaFile);
        }
    }

    /**
     * Prueba para asociar un archivo a una mascota correctamente.
     */
    @Test
    void testAddPhotograph() throws EntityNotFoundException {
        // Crear nuevo mediaFile sin mascota asociada
        MediaFileEntity newMediaFile = factory.manufacturePojo(MediaFileEntity.class);
        newMediaFile.setUrl("https://example.com/nuevo.jpg");
        newMediaFile.setPet(null);
        newMediaFile.setShelter(null);
        entityManager.persist(newMediaFile);

        // Asociar a la segunda mascota
        PetEntity pet = petList.get(1);
        PetEntity result = petMediaFileService.addPhotograph(newMediaFile.getId(), pet.getId());

        assertNotNull(result);
        assertTrue(result.getPhotographs().stream()
                .anyMatch(f -> f.getId().equals(newMediaFile.getId())));
    }

    /**
     * Prueba para asociar un archivo con un mediaFileId inválido.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testAddPhotographInvalidMediaFile() {
        assertThrows(EntityNotFoundException.class, () -> {
            petMediaFileService.addPhotograph(0L, petList.get(0).getId());
        });
    }

    /**
     * Prueba para asociar un archivo con un petId inválido.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testAddPhotographInvalidPet() {
        assertThrows(EntityNotFoundException.class, () -> {
            petMediaFileService.addPhotograph(mediaFileList.get(0).getId(), 0L);
        });
    }

    /**
     * Prueba para obtener todos los archivos de una mascota.
     */
    @Test
    void testGetPhotographs() throws EntityNotFoundException {
        PetEntity pet = petList.get(0);
        List<MediaFileEntity> result = petMediaFileService.getPhotographs(pet.getId());

        assertNotNull(result);
        assertEquals(3, result.size()); // se insertaron 3 en insertData
    }

    /**
     * Prueba para obtener archivos de una mascota sin fotografías.
     */
    @Test
    void testGetPhotographsEmptyList() throws EntityNotFoundException {
        PetEntity pet = petList.get(1); // esta mascota no tiene fotos
        List<MediaFileEntity> result = petMediaFileService.getPhotographs(pet.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Prueba para obtener archivos de una mascota con petId inválido.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testGetPhotographsInvalidPet() {
        assertThrows(EntityNotFoundException.class, () -> {
            petMediaFileService.getPhotographs(0L);
        });
    }
}