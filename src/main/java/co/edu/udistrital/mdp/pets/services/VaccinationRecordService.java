package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VaccinationRecordService {

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Transactional
    public VaccinationRecordEntity createVaccinationRecord(VaccinationRecordEntity vaccinationRecordEntity) throws IllegalOperationException {
        log.info("Inicia proceso de creacion de registro de vacunacion ");

        if (isStringValid(vaccinationRecordEntity.getVaccineName()) && vaccinationRecordEntity.getVaccineDate() != null
                && vaccinationRecordEntity.getNextDosesDate() != null) {
            return vaccinationRecordRepository.save(vaccinationRecordEntity);
        } else {
            throw new IllegalOperationException("todos los campos deben estar llenos");
        }
    }

    @Transactional
    public VaccinationRecordEntity update(Long id, VaccinationRecordEntity vaccinationRecordEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de actualizacion del registro de vacunacion");

        Optional<VaccinationRecordEntity> vaccOptional = vaccinationRecordRepository.findById(id);
        if (vaccOptional.isEmpty()) {
            throw new EntityNotFoundException("Registro de vacunacion no encontrado");
        }

        VaccinationRecordEntity existingVaccinationRecordEntity = vaccOptional.get();

        if (vaccinationRecordEntity.getVaccineName() != null) {
            existingVaccinationRecordEntity.setVaccineName(vaccinationRecordEntity.getVaccineName());
        }
        if (vaccinationRecordEntity.getVaccineDate() != null) {
            existingVaccinationRecordEntity.setVaccineDate(vaccinationRecordEntity.getVaccineDate());
        }
        if (vaccinationRecordEntity.getNextDosesDate() != null) {
            existingVaccinationRecordEntity.setNextDosesDate(vaccinationRecordEntity.getNextDosesDate());
        }

        log.info("Termina proceso de actualización del registro de vacunacion");
        return vaccinationRecordRepository.save(existingVaccinationRecordEntity);

    }

    @Transactional
    public void deleteVaccinationRecord(Long id) throws EntityNotFoundException {
        log.info("inicia proceso de borrar registro de vacunacion");
        Optional<VaccinationRecordEntity> vaccOptional = vaccinationRecordRepository.findById(id);
        if (vaccOptional.isEmpty()) {
            throw new EntityNotFoundException("registro de vacunacion no encontrada");
        }

        vaccinationRecordRepository.deleteById(id);
        log.info("Proceso de borrado terminado");
    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }
}
