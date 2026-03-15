package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalEventService {

    @Autowired
    MedicalEventRepository medicalEventRepository;

    @Transactional
    public MedicalEventEntity createMedicalEvent(MedicalEventEntity medicalEventEntity) throws IllegalOperationException {
        log.info("Iniciando creacion de evento medico");
        if(isStringValid(medicalEventEntity.getDescription())&& medicalEventEntity.getDate()!=null){
            return medicalEventRepository.save(medicalEventEntity);
        }else{
            throw new IllegalOperationException("Todos los campos deben estar llenos");
        }
    }

    @Transactional
    public MedicalEventEntity updateMedicalEventEntity(Long id, MedicalEventEntity medicalEventEntity)throws EntityNotFoundException{
        log.info("Iniciando proceso de actuaclizacion del evento medico");

        Optional<MedicalEventEntity> medicalEventOptional = medicalEventRepository.findById(id);
        if (medicalEventOptional.isEmpty()) {
            throw new EntityNotFoundException("Evento medico no encontrado");
        }

        MedicalEventEntity existingMedicalEvent = medicalEventOptional.get();
        if(medicalEventEntity.getDescription()!=null){
            existingMedicalEvent.setDescription(medicalEventEntity.getDescription());
        }
        if(medicalEventEntity.getDate()!=null){
            existingMedicalEvent.setDate(medicalEventEntity.getDate());
        }

        log.info("termina proceso de actualizacion de evento medico");
        return medicalEventRepository.save(existingMedicalEvent);
    }

    @Transactional
    public void delateMedicalEvent(Long id) throws EntityNotFoundException{
        log.info("Inicia proceso de eliminacion de evento medico");
        Optional<MedicalEventEntity> medicalEventOptional = medicalEventRepository.findById(id);
        if (medicalEventOptional.isEmpty()) {
            throw new EntityNotFoundException("evento medico no encontrado");
        }

        medicalEventRepository.deleteById(id);
        log.info("Proceso de borrado terminado");
    }


    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }

}
