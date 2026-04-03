package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    // CREAR ADOPTANTE
    public AdopterEntity createAdopter(AdopterEntity adopter) {
        return adopterRepository.save(adopter);
    }

    // OBTENER POR ID
    public AdopterEntity getAdopter(Long id) throws EntityNotFoundException {
        Optional<AdopterEntity> adopter = adopterRepository.findById(id);

        if (adopter.isEmpty()) {
            throw new EntityNotFoundException("Adopter not found");
        }

        return adopter.get();
    }

    // LISTAR TODOS
    public List<AdopterEntity> getAdopters() {
        return adopterRepository.findAll();
    }

    // ACTUALIZAR
    public AdopterEntity updateAdopter(Long id, AdopterEntity newAdopter) throws EntityNotFoundException {

        AdopterEntity adopter = getAdopter(id);

        adopter.setHasChildren(newAdopter.getHasChildren());
        adopter.setHasPets(newAdopter.getHasPets());

        return adopterRepository.save(adopter);
    }

    // ELIMINAR
    public void deleteAdopter(Long id) throws EntityNotFoundException {
        AdopterEntity adopter = getAdopter(id);
        adopterRepository.delete(adopter);
    }
}