package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionDefEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionDefRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdoptionDefService {

    @Autowired
    private AdoptionDefRepository adoptionDefRepository;

    // CREAR ADOPCIÓN DEFINITIVA
    public AdoptionDefEntity createAdoptionDef(AdoptionDefEntity adoptionDef) {
        return adoptionDefRepository.save(adoptionDef);
    }

    // OBTENER POR ID
    public AdoptionDefEntity getAdoptionDef(Long id) throws EntityNotFoundException {

        Optional<AdoptionDefEntity> adoptionDef = adoptionDefRepository.findById(id);

        if (adoptionDef.isEmpty()) {
            throw new EntityNotFoundException("Adoption definition not found");
        }

        return adoptionDef.get();
    }

    // LISTAR TODOS
    public List<AdoptionDefEntity> getAdoptionDefs() {
        return adoptionDefRepository.findAll();
    }

    // ACTUALIZAR
    public AdoptionDefEntity updateAdoptionDef(Long id, AdoptionDefEntity newAdoptionDef)
            throws EntityNotFoundException {

        AdoptionDefEntity adoptionDef = getAdoptionDef(id);

        adoptionDef.setAdoption(newAdoptionDef.getAdoption());

        return adoptionDefRepository.save(adoptionDef);
    }

    // ELIMINAR
    public void deleteAdoptionDef(Long id) throws EntityNotFoundException {

        AdoptionDefEntity adoptionDef = getAdoptionDef(id);
        adoptionDefRepository.delete(adoptionDef);
    }
}