package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.BackgroundEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.BackgroundRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BackgroundService {

    @Autowired
    private BackgroundRepository backgroundRepository;

    @Transactional
    public BackgroundEntity craeteBackground(BackgroundEntity backgroundEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creacion de antecedente");

        //revisa que el archivo tenga todos los datos llenos
        if (backgroundEntity.getDate() != null && isStringValid(backgroundEntity.getDescription())) {

            return backgroundRepository.save(backgroundEntity);

        } else {
            throw new IllegalOperationException("todos los campos tienen que estar llenos");
        }

    }

    @Transactional
    public BackgroundEntity updateBackground(Long id, BackgroundEntity backgroundEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de actualización de antecedente");

        Optional<BackgroundEntity> background = backgroundRepository.findById(id);
        if (background.isEmpty()) {
            throw new EntityNotFoundException("Mascota no encontrada");
        }

        BackgroundEntity existingbBackground = background.get();

        if (backgroundEntity.getDate() != null) {
            existingbBackground.setDate(backgroundEntity.getDate());
        }

        if (backgroundEntity.getDescription() != null) {
            existingbBackground.setDescription(backgroundEntity.getDescription());
        }


        log.info("Termina proceso de actualización de antecedente");
        return backgroundRepository.save(existingbBackground);
    }

    @Transactional
    public void deleteBackground(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("inicia proceso de borrar antecedente");
        Optional<BackgroundEntity> backgroundEntity = backgroundRepository.findById(id);
        if (backgroundEntity.isEmpty()) {
            throw new EntityNotFoundException("archivo no encontrada");
        }

        backgroundRepository.deleteById(id);
        log.info("Proceso de borrado terminado");
    }


    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }
}
