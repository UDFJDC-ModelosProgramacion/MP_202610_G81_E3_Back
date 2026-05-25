package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetMedicalEventService {

    @Autowired
    private MedicalEventRepository medicalEventRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public PetEntity addMedicalEvent(Long medicalId, Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de asociacion de evento medico con mascota");
        Optional<MedicalEventEntity>medicalEventEntity=medicalEventRepository.findById(medicalId);
        if(medicalEventEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra evento medico");
        }
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        petEntity.get().getMedicalEvents().add(medicalEventEntity.get()); // se agrega el evento medico a la mascota
        medicalEventEntity.get().setPet(petEntity.get());
        log.info("Termina proceso de asociacion de evento medico y mascota");
        return petEntity.get();
    }


    @Transactional
    public List<MedicalEventEntity> getMedicalEvents(Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de consulta de eventos medicos");
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        log.info("Finaliza proceso de consulta");
        return petEntity.get().getMedicalEvents();
    }
}
