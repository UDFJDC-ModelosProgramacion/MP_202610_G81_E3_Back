package co.edu.udistrital.mdp.pets.services;

// ===== IMPORTS INICIO =====
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionStatus;

import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;

import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Service
@Transactional
public class AdoptionService {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== DEPENDENCIAS INICIO =====
    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private PetRepository petRepository;
    // ===== DEPENDENCIAS FIN =====


    // ===== MÉTODOS CRUD INICIO =====

    public AdoptionEntity createAdoption(Long adopterId, Long petId, AdoptionEntity adoption) throws EntityNotFoundException {

        AdopterEntity adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"));

        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

        // ===== RELACIONES =====
        adoption.setAdopter(adopter);
        adoption.setPet(pet);

        // ===== LÓGICA =====
        adoption.setAdoptionDate(LocalDate.now());
        adoption.setTrialStartDate(LocalDate.now());
        adoption.setStatus(AdoptionStatus.IN_TRIAL);

        return adoptionRepository.save(adoption);
    }


    public AdoptionEntity getAdoption(Long id) throws EntityNotFoundException {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
    }


    public List<AdoptionEntity> getAdoptions() {
        return adoptionRepository.findAll();
    }


    public AdoptionEntity updateAdoption(Long id, AdoptionEntity newAdoption) throws EntityNotFoundException {

        AdoptionEntity adoption = getAdoption(id);

        // ===== ACTUALIZACIÓN CONTROLADA =====
        adoption.setTrialEndDate(newAdoption.getTrialEndDate());
        adoption.setStatus(newAdoption.getStatus());

        return adoptionRepository.save(adoption);
    }


    public void deleteAdoption(Long id) throws EntityNotFoundException {
        AdoptionEntity adoption = getAdoption(id);
        adoptionRepository.delete(adoption);
    }

    // ===== MÉTODOS CRUD FIN =====

}