package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public PetEntity createPet(PetEntity petEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creacion de mascota (Pet)");

        //revisa que la mascota tenga todos los datos llenos
        if (isStringValid(petEntity.getName()) && isStringValid(petEntity.getSpecies()) && isStringValid(petEntity.getBreed())
                && isStringValid(petEntity.getSex()) && petEntity.getSize() != null && petEntity.getArriveToShelter() != null
                && isStringValid(petEntity.getSpecificRequirements()) && !petEntity.getPhotographes().isEmpty()) {

            return petRepository.save(petEntity);

        } else {
            throw new IllegalOperationException("All fields must be filled in");
        }

    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }

}
