package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MediaFileRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public MediaFileEntity createMediaFile(MediaFileEntity mediaFileEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creacion de archivo");

        //revisa que el archivo tenga todos los datos llenos
        if (mediaFileEntity.getMediaFileType() != null && isStringValid(mediaFileEntity.getUrl())) {

            return mediaFileRepository.save(mediaFileEntity);

        } else {
            throw new IllegalOperationException("todos los campos tienen que estar llenos");
        }

    }

    @Transactional
    public MediaFileEntity updateMediaFile(Long id, MediaFileEntity mediaFileEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de actualización de archivo");

        Optional<MediaFileEntity> mediaFile = mediaFileRepository.findById(id);
        if (mediaFile.isEmpty()) {
            throw new EntityNotFoundException("Mascota no encontrada");
        }

        MediaFileEntity existingMediaFile = mediaFile.get();

        if (mediaFileEntity.getUrl() != null) {
            existingMediaFile.setUrl(mediaFileEntity.getUrl());
        }

        if (mediaFileEntity.getMediaFileType() != null) {
            existingMediaFile.setMediaFileType(mediaFileEntity.getMediaFileType());
        }


        log.info("Termina proceso de actualización de archivo");
        return mediaFileRepository.save(existingMediaFile);
    }

    @Transactional
    public void deleteMediaFile(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("inicia proceso de borrar archivo");
        Optional<MediaFileEntity> mediaFileEntity = mediaFileRepository.findById(id);
        if (mediaFileEntity.isEmpty()) {
            throw new EntityNotFoundException("archivo no encontrada");
        }

        mediaFileRepository.deleteById(id);
        log.info("Proceso de borrado terminado");
    }

    @Transactional
    public MediaFileEntity getMediaFile(Long petId, Long mediaFileId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar archivo con id = {0} de la mascota con id = " + petId, mediaFileId);
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty())
            throw new EntityNotFoundException("Mascota no encontrada");

        Optional<MediaFileEntity> mediaFileEntity = mediaFileRepository.findById(mediaFileId);
        if (mediaFileEntity.isEmpty())
            throw new EntityNotFoundException("Archivo no encontrado");

        log.info("Termina proceso de consultar archivo con id = {0} de la mascota con id = " + petId, mediaFileId);
        return mediaFileRepository.findByPetIdAndId(petId, mediaFileId);
    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }
}
