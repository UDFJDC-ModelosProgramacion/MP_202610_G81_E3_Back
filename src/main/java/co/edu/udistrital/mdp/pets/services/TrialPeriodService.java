package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.TrialPeriodEntity;
import co.edu.udistrital.mdp.pets.repositories.TrialPeriodRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TrialPeriodService {

    @Autowired
    private TrialPeriodRepository trialPeriodRepository;

    // CREAR TRIAL PERIOD
    public TrialPeriodEntity createTrial(TrialPeriodEntity trial) {
        return trialPeriodRepository.save(trial);
    }

    // OBTENER POR ID
    public TrialPeriodEntity getTrial(Long id) throws EntityNotFoundException {

        Optional<TrialPeriodEntity> trial = trialPeriodRepository.findById(id);

        if (trial.isEmpty()) {
            throw new EntityNotFoundException("Trial period not found");
        }

        return trial.get();
    }

    // LISTAR TODOS
    public List<TrialPeriodEntity> getTrials() {
        return trialPeriodRepository.findAll();
    }

    // ACTUALIZAR (solo relación)
    public TrialPeriodEntity updateTrial(Long id, TrialPeriodEntity newTrial) throws EntityNotFoundException {

        TrialPeriodEntity trial = getTrial(id);

        trial.setAdoption(newTrial.getAdoption());

        return trialPeriodRepository.save(trial);
    }

    // ELIMINAR
    public void deleteTrial(Long id) throws EntityNotFoundException {

        TrialPeriodEntity trial = getTrial(id);
        trialPeriodRepository.delete(trial);
    }
}