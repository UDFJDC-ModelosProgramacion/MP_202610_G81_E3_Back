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

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.entities.MediaFileType;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MediaFileService.class)
class MediaFileTest {

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

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
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MediaFileEntity entity = factory.manufacturePojo(MediaFileEntity.class);
            entity.setUrl("https://example.com/file_" + i + ".jpg");
            entity.setMediaFileType(MediaFileType.PHOTOGRAPH);
            entity.setPet(null);
            entity.setShelter(null);
            entityManager.persist(entity);
            mediaFileList.add(entity);
        }
    }

    /**
     * Prueba para crear un MediaFile con datos válidos.
     */
    @Test
    void testCreateMediaFile() throws IllegalOperationException, EntityNotFoundException {
        MediaFileEntity newEntity = factory.manufacturePojo(MediaFileEntity.class);
        newEntity.setUrl("https://example.com/nuevo.jpg");
        newEntity.setMediaFileType(MediaFileType.PHOTOGRAPH);
        newEntity.setPet(null);
        newEntity.setShelter(null);

        MediaFileEntity result = mediaFileService.createMediaFile(newEntity);
        assertNotNull(result);

        MediaFileEntity stored = entityManager.find(MediaFileEntity.class, result.getId());
        assertNotNull(stored);
        assertEquals(newEntity.getUrl(), stored.getUrl());
        assertEquals(newEntity.getMediaFileType(), stored.getMediaFileType());
    }

    /**
     * Prueba para crear un MediaFile con url nula.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMediaFileNullUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            MediaFileEntity newEntity = factory.manufacturePojo(MediaFileEntity.class);
            newEntity.setUrl(null);
            newEntity.setMediaFileType(MediaFileType.PHOTOGRAPH);
            newEntity.setPet(null);
            newEntity.setShelter(null);
            mediaFileService.createMediaFile(newEntity);
        });
    }

    /**
     * Prueba para crear un MediaFile con url vacía.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMediaFileEmptyUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            MediaFileEntity newEntity = factory.manufacturePojo(MediaFileEntity.class);
            newEntity.setUrl("");
            newEntity.setMediaFileType(MediaFileType.PHOTOGRAPH);
            newEntity.setPet(null);
            newEntity.setShelter(null);
            mediaFileService.createMediaFile(newEntity);
        });
    }

    /**
     * Prueba para crear un MediaFile con mediaFileType nulo.
     * Debe lanzar IllegalOperationException.
     */
    @Test
    void testCreateMediaFileNullType() {
        assertThrows(IllegalOperationException.class, () -> {
            MediaFileEntity newEntity = factory.manufacturePojo(MediaFileEntity.class);
            newEntity.setUrl("https://example.com/nuevo.jpg");
            newEntity.setMediaFileType(null);
            newEntity.setPet(null);
            newEntity.setShelter(null);
            mediaFileService.createMediaFile(newEntity);
        });
    }

    /**
     * Prueba para actualizar un MediaFile existente con todos los campos.
     */
    @Test
    void testUpdateMediaFile() throws EntityNotFoundException {
        MediaFileEntity existing = mediaFileList.get(0);

        MediaFileEntity updatedData = factory.manufacturePojo(MediaFileEntity.class);
        updatedData.setUrl("https://example.com/actualizado.jpg");
        updatedData.setMediaFileType(MediaFileType.VIDEO);
        updatedData.setPet(null);
        updatedData.setShelter(null);

        MediaFileEntity result = mediaFileService.updateMediaFile(existing.getId(), updatedData);
        assertNotNull(result);

        MediaFileEntity stored = entityManager.find(MediaFileEntity.class, existing.getId());
        assertEquals("https://example.com/actualizado.jpg", stored.getUrl());
        assertEquals(MediaFileType.VIDEO, stored.getMediaFileType());
    }

    /**
     * Prueba para actualizar solo la url.
     * Los campos nulos no deben modificar los valores originales.
     */
    @Test
    void testUpdateMediaFileOnlyUrl() throws EntityNotFoundException {
        MediaFileEntity existing = mediaFileList.get(1);
        MediaFileType originalType = existing.getMediaFileType();

        MediaFileEntity partialUpdate = new MediaFileEntity();
        partialUpdate.setUrl("https://example.com/solo-url.jpg");

        mediaFileService.updateMediaFile(existing.getId(), partialUpdate);

        MediaFileEntity stored = entityManager.find(MediaFileEntity.class, existing.getId());
        assertEquals("https://example.com/solo-url.jpg", stored.getUrl());
        assertEquals(originalType, stored.getMediaFileType());
    }

    /**
     * Prueba para actualizar solo el mediaFileType.
     * Los campos nulos no deben modificar los valores originales.
     */
    @Test
    void testUpdateMediaFileOnlyType() throws EntityNotFoundException {
        MediaFileEntity existing = mediaFileList.get(2);
        String originalUrl = existing.getUrl();

        MediaFileEntity partialUpdate = new MediaFileEntity();
        partialUpdate.setMediaFileType(MediaFileType.VIDEO);

        mediaFileService.updateMediaFile(existing.getId(), partialUpdate);

        MediaFileEntity stored = entityManager.find(MediaFileEntity.class, existing.getId());
        assertEquals(originalUrl, stored.getUrl());
        assertEquals(MediaFileType.VIDEO, stored.getMediaFileType());
    }

    /**
     * Prueba para actualizar un MediaFile que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testUpdateInvalidMediaFile() {
        assertThrows(EntityNotFoundException.class, () -> {
            MediaFileEntity updatedData = factory.manufacturePojo(MediaFileEntity.class);
            mediaFileService.updateMediaFile(0L, updatedData);
        });
    }

    /**
     * Prueba para eliminar un MediaFile existente.
     */
    @Test
    void testDeleteMediaFile() throws Exception {
        MediaFileEntity entity = mediaFileList.get(0);
        mediaFileService.deleteMediaFile(entity.getId());

        MediaFileEntity deleted = entityManager.find(MediaFileEntity.class, entity.getId());
        assertNull(deleted);
    }

    /**
     * Prueba para eliminar un MediaFile que no existe.
     * Debe lanzar EntityNotFoundException.
     */
    @Test
    void testDeleteInvalidMediaFile() {
        assertThrows(EntityNotFoundException.class, () -> {
            mediaFileService.deleteMediaFile(0L);
        });
    }
}