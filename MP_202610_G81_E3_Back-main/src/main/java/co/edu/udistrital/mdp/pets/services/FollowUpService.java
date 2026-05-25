package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.FollowUpRepository;

@Service
public class FollowUpService {

    @Autowired
    private FollowUpRepository followUpRepository;

    @Transactional
    public FollowUpEntity createFollowUp(FollowUpEntity followUp) throws IllegalOperationException {
        if (followUp.getObservation() == null || followUp.getObservation().trim().isEmpty()) {
            throw new IllegalOperationException("La observación del seguimiento no puede ser nula o vacía.");
        }
        if (followUp.getVisitDate() != null && followUp.getVisitDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException("La fecha de visita no puede ser en el futuro.");
        }
        return followUpRepository.save(followUp);
    }

    @Transactional(readOnly = true)
    public List<FollowUpEntity> getFollowUps() {
        return followUpRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FollowUpEntity getFollowUp(Long id) throws EntityNotFoundException {
        Optional<FollowUpEntity> followUp = followUpRepository.findById(id);
        if (followUp.isEmpty()) {
            throw new EntityNotFoundException("El seguimiento con id " + id + " no fue encontrado.");
        }
        return followUp.get();
    }

    @Transactional
    public FollowUpEntity updateFollowUp(Long id, FollowUpEntity followUp)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<FollowUpEntity> existing = followUpRepository.findById(id);
        if (existing.isEmpty()) {
            throw new EntityNotFoundException("El seguimiento con id " + id + " no fue encontrado.");
        }
        if (followUp.getObservation() == null || followUp.getObservation().trim().isEmpty()) {
            throw new IllegalOperationException("La observación del seguimiento no puede ser nula o vacía.");
        }
        if (followUp.getVisitDate() != null && followUp.getVisitDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException("La fecha de visita no puede ser en el futuro.");
        }
        followUp.setId(id);
        return followUpRepository.save(followUp);
    }

    @Transactional
    public void deleteFollowUp(Long id) throws EntityNotFoundException {
        Optional<FollowUpEntity> followUp = followUpRepository.findById(id);
        if (followUp.isEmpty()) {
            throw new EntityNotFoundException("El seguimiento con id " + id + " no fue encontrado.");
        }
        followUpRepository.deleteById(id);
    }
}