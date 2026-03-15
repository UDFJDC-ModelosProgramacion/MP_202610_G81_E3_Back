package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterService {

    @Autowired
    ShelterRepository shelterRepository;

    //Definir regex para validar correo.
    String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

    //Metodo para crear un refugio.
    @Transactional
    public ShelterEntity createShelter(ShelterEntity shelterEntity) throws EntityNotFoundException, IllegalOperationException {
    
        log.info("Start shelter creation...");

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

        //Validar si solo hay un refugio con ese nombre.

        if(shelterRepository.findByName(shelterEntity.getName()).isPresent())
            throw new IllegalOperationException("There is already a shelter with that name.");

        //Validar si solo hay un refugio con ese email.
        if(shelterRepository.findByEmail(shelterEntity.getEmail()).isPresent())
            throw new IllegalOperationException("There is already a shelter with that email.");

        //Validar si el formato del correo es correcto.
        if(!shelterEntity.getEmail().matches(regex))
            throw new IllegalOperationException("Email format isn't valid.");

        //Si cumple con todo se permitirá crear un refugio.
        return shelterRepository.save(shelterEntity);
    }
}
