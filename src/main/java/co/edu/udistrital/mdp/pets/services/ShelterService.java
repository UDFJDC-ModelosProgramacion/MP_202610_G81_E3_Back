package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Slf4j
@Service
public class ShelterService {
    @Autowired
    //Repositorio de refugios, para persistencia.
    private ShelterRepository shelterRepository;

    //Definir regex para validar correo.
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

    //Metodo privado para evaluar si son válidos o no los datos del refugio.
    private void validateShelter(ShelterEntity shelterEntity)
    throws IllegalOperationException {
        //Validar si el nombre es nulo.
        if(shelterEntity.getName() == null)
            throw new IllegalOperationException("Name isn't valid.");

        //Validar si la ciudad es nula.
        if(shelterEntity.getCity() == null)
            throw new IllegalOperationException("City isn't valid.");

        //Validar si la dirección es nula.
        if(shelterEntity.getAddress() == null)
            throw new IllegalOperationException("Adress isn't valid.");

        //Validar si el correo es nulo.
        if(shelterEntity.getEmail() == null)
            throw new IllegalOperationException("Email isn't valid.");

        //Validar si el formato del correo es correcto.
        if(!shelterEntity.getEmail().matches(regex))
            throw new IllegalOperationException("Email format isn't valid.");
    }

    //Metodo para crear un refugio.
    @Transactional
    public ShelterEntity createShelter(ShelterEntity shelterEntity) 
    throws EntityNotFoundException, IllegalOperationException {
    
        log.info("Start shelter creation...");
        //Usar el método privado para evitar repetir código.
        validateShelter(shelterEntity);

        //Validar si solo hay un refugio con ese nombre.
        if(shelterRepository.findByName(shelterEntity.getName()).isPresent())
            throw new IllegalOperationException("There is already a shelter with that name.");

        //Validar si solo hay un refugio con ese email.
        if(shelterRepository.findByEmail(shelterEntity.getEmail()).isPresent())
            throw new IllegalOperationException("There is already a shelter with that email.");

        //Si cumple con todo se permitirá crear un refugio.
        return shelterRepository.save(shelterEntity);
    }

    //Metodo para actualizar un refugio.
    @Transactional
    public ShelterEntity updateShelter(long shelterId, ShelterEntity shelter) 
     throws EntityNotFoundException, IllegalOperationException {
        log.info("Start update shelter with id: {}", shelterId);
        //Busca el refugio.
        Optional<ShelterEntity> shelterEntity = shelterRepository.findById(shelterId);

        //Verifica su existencia.
        if (shelterEntity.isEmpty())
            throw new EntityNotFoundException("Shelter not found.");
        //Obtiene el refugio a actualizar.
        ShelterEntity shelterToUpdate = shelterEntity.get();
        
        //Validacion de datos.
        validateShelter(shelter);

        //Validar unicidad de nombre.
        Optional<ShelterEntity> nameCheck = shelterRepository.findByName(shelter.getName());
        if (nameCheck.isPresent() && !nameCheck.get().getId().equals(shelterId)) {
            throw new IllegalOperationException("There is already a shelter with that name.");
        }

        //Validar unicidad de email.
        Optional<ShelterEntity> emailCheck = shelterRepository.findByEmail(shelter.getEmail());
        if (emailCheck.isPresent() && !emailCheck.get().getId().equals(shelterId)) {
            throw new IllegalOperationException("There is already a shelter with that email.");
        }

        //Actualizar si cumple con todas las condiciones.
        shelterToUpdate.setName(shelter.getName());
        shelterToUpdate.setCity(shelter.getCity());
        shelterToUpdate.setAddress(shelter.getAddress());
        shelterToUpdate.setEmail(shelter.getEmail());

        log.info("End update shelter with the id: {}", shelterId);
        return shelterRepository.save(shelterToUpdate);
    }

    //Metodo para borrar un refugio existente.
    @Transactional
    public void deleteShelter(Long shelterId) 
    throws EntityNotFoundException, IllegalOperationException{
        log.info("Start shelter delete with the id: {}", shelterId);

        //Busca el refugio.
        Optional<ShelterEntity> shelterEntity = shelterRepository.findById(shelterId);

        //Verifica que el refugio exista.
        if(shelterEntity.isEmpty())
            throw new EntityNotFoundException("Shelter not found.");

        //Aqui hay que agregar la condicion de los eventos.
        //Para ello se trabaja en otra rama los shelter event service.
        shelterRepository.deleteById(shelterId);
        log.info("End delete shelter process with id: {}", shelterId);
    }
}
