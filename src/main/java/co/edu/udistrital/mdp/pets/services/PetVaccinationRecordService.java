package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetVaccinationRecordService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;


    @Transactional
    public PetEntity addVaccinationRecord(Long petId, Long vaccinationId)throws EntityNotFoundException{
        log.info("Iniciando asociacion entre mascota y registro de vacunacion");
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("Mascota no encontrada");
        }
        Optional<VaccinationRecordEntity>vaccinationRecordEntity=vaccinationRecordRepository.findById(vaccinationId);
        if(vaccinationRecordEntity.isEmpty()){
            throw new EntityNotFoundException("registro de vacunacion no encontrado");
        }
        petEntity.get().getVaccinationRecords().add(vaccinationRecordEntity.get()); //se agrega el registro de vacunacion a la mascota
        log.info("Finaliza proceso de asociacion entre mascota y registro de vacunacion");
        return petEntity.get();
    }

    @Transactional
    public List<VaccinationRecordEntity> getVaccinationRecords(Long petId)throws EntityNotFoundException{
        log.info("Iniciando consutla de registros de vacunacion");
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("Mascota no encontrada");
        }
        log.info("Finaliza consulta de registro de vacunacion");
        return petEntity.get().getVaccinationRecords();
    }


}
