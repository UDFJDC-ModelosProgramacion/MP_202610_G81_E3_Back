package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.services.interfaces.IAAdoptar;

import co.edu.udistrital.mdp.pets.repositories.*;
import co.edu.udistrital.mdp.pets.entities.*;

@Service
public class AdoptarService implements IAAdoptar {

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private TrialPeriodRepository trialPeriodRepository;

    @Autowired
    private AdoptionDefRepository adoptionDefRepository;

    @Override
    public void adopt(Long adopterId, Long petId) {

        // 1. Buscar adoptante
        AdopterEntity adopter = adopterRepository.findById(adopterId)
                .orElseThrow(() -> new RuntimeException("Adopter no encontrado"));

        // 2. Buscar mascota
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet no encontrado"));

        // 3. Crear adopción base
        AdoptionEntity adoption = new AdoptionEntity();
        adoption.setAdopter(adopter);
        adoption.setPet(pet);

        adoptionRepository.save(adoption);

        // 4. DECISIÓN DEL FLUJO
        boolean necesitaPeriodoPrueba = true;

        if (necesitaPeriodoPrueba) {

            TrialPeriodEntity trial = new TrialPeriodEntity();
            trial.setAdoption(adoption);

            trialPeriodRepository.save(trial);

        } else {

            AdoptionDefEntity def = new AdoptionDefEntity();
            def.setAdoption(adoption);

            adoptionDefRepository.save(def);
        }
    }
}