package co.edu.udistrital.mdp.pets.services;

import java.util.List;
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
        if (petEntity.getShelter() == null || petEntity.getShelter().getId() == null) {
            throw new IllegalOperationException("La mascota debe estar asociada a un refugio");
        }
        if (isStringValid(petEntity.getName()) && isStringValid(petEntity.getSpecies()) 
                && isStringValid(petEntity.getBreed()) && isStringValid(petEntity.getSex()) 
                && isStringValid(petEntity.getSize()) 
                && isStringValid(petEntity.getRequiredSpace())
                && petEntity.getArriveToShelterDate() != null
                && isStringValid(petEntity.getSpecificRequirements())) {
            return petRepository.save(petEntity);
        } else {
            throw new IllegalOperationException("Todos los campos tienen que estar llenos");
        }
//&& !petEntity.getPhotographes().isEmpty()
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
        if (petEntity.getArriveToShelterDate() != null) {
            existingPet.setArriveToShelterDate(petEntity.getArriveToShelterDate());
        }

        if (petEntity.getSpecificRequirements() != null) {
            existingPet.setSpecificRequirements(petEntity.getSpecificRequirements());
        }
        
        if (petEntity.getRequiredSpace() != null) {
            existingPet.setRequiredSpace(petEntity.getRequiredSpace());
        }

        log.info("Termina proceso de actualización de mascota (Pet)");
        return petRepository.save(existingPet);
    }

    @Transactional
    public void deletePet(Long petId) throws EntityNotFoundException, IllegalOperationException {
        log.info("inicia proceso de borrar mascota");
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()) {
            throw new EntityNotFoundException("mascota no encontrada");
        }

        petRepository.deleteById(petId);
        log.info("Proceso de borrado terminado");
    }

    @Transactional
    public List<PetEntity> getPets() {
        log.info("Inicia proceso de consultar todas las macotas");
        return petRepository.findAll();
    }

    @Transactional
    public PetEntity getPet(Long petId) throws EntityNotFoundException {
        log.info("Inicia proceso de consula de la mascota", petId);
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()) {
            throw new EntityNotFoundException("mascota no encontrada");
        }
        log.info("Termina proceso de consultar la mascota con id = {0}", petId);
        return petEntity.get();
    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }

    @Transactional
    public List<PetEntity> searchPets(String keyword, List<String> filters) {
        String species = null;
        Integer minAge = null;
        Integer maxAge = null;
        String size = null;
        String requirements = null;
        String requiredSpace = null;

        if (filters != null) {
            for (String filter : filters) {
                // Especie
                if (filter.equalsIgnoreCase("Perro")
                        || filter.equalsIgnoreCase("Gato")
                        || filter.equalsIgnoreCase("Otros")) {
                    species = filter;
                }

                // Edad
                switch (filter) {
                    case "Cachorros" -> {
                        minAge = 0;
                        maxAge = 2;
                    }
                    case "Jovenes" -> {
                        minAge = 2;
                        maxAge = 5;
                    }
                    case "Adultos" -> {
                        minAge = 5;
                        maxAge = 10;
                    }
                    case "Senior" -> {
                        minAge = 10;
                        maxAge = null;
                    }
                }

                // Tamaño
                if (filter.equalsIgnoreCase("Pequeño")
                        || filter.equalsIgnoreCase("Mediano")
                        || filter.equalsIgnoreCase("Grande")) {
                    size = filter;
                }

                // Requerimientos
                if (filter.equalsIgnoreCase("Casa")
                        || filter.equalsIgnoreCase("Apartamento")) {
                    requiredSpace = filter;
                }
            }
        }

        return petRepository.searchByKeywordAndFilters(
                keyword != null ? keyword : "",
                species,
                minAge,
                maxAge,
                size,
                requirements,
                requiredSpace
        );
    }
}
