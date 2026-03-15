package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

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
            throw new IllegalOperationException("todos los campos tienen que estar llenos");
        }

    }

    @Transactional
    public PetEntity updatePet(Long id, PetEntity petEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de actualización de mascota (Pet)");

        Optional<PetEntity> petOptional = petRepository.findById(id);
        if (petOptional.isEmpty()) {
            throw new EntityNotFoundException("Mascota no encontrada");
        }

        PetEntity existingPet = petOptional.get();

        if (petEntity.getName() != null) {
            existingPet.setName(petEntity.getName());
        }

        if (petEntity.getSpecies() != null) {
            existingPet.setSpecies(petEntity.getSpecies());
        }

        if (petEntity.getBreed() != null) {
            existingPet.setBreed(petEntity.getBreed());
        }
        if (petEntity.getAge() != null) {
            existingPet.setAge(petEntity.getAge());
        }
        if (petEntity.getSex() != null) {
            existingPet.setSex(petEntity.getSex());
        }

        if (petEntity.getSize() != null) {
            existingPet.setSize(petEntity.getSize());
        }
        if (petEntity.getTemperament() != null) {
            existingPet.setTemperament(petEntity.getTemperament());
        }
        if (petEntity.getArriveToShelter() != null) {
            existingPet.setArriveToShelter(petEntity.getArriveToShelter());
        }

        if (petEntity.getSpecificRequirements() != null) {
            existingPet.setSpecificRequirements(petEntity.getSpecificRequirements());
        }

        log.info("Termina proceso de actualización de mascota (Pet)");
        return petRepository.save(existingPet);
    }

    @Transactional
    public void delatePet(Long petId) throws EntityNotFoundException, IllegalOperationException {
        log.info("inicia proceso de borrar mascota");
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()) {
            throw new EntityNotFoundException("mascota no encontrada");
        }

        petRepository.deleteById(petId);
        log.info("Proceso de borrado terminado");
    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }

}
