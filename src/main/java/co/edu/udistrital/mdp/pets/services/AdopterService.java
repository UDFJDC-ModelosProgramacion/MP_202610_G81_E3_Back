package co.edu.udistrital.mdp.pets.services;

// ===== IMPORTS INICIO =====
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

import jakarta.transaction.Transactional;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Service
@Transactional
public class AdopterService {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== DEPENDENCIAS INICIO =====
    @Autowired
    private AdopterRepository adopterRepository;
    // ===== DEPENDENCIAS FIN =====


    // ===== MÉTODOS CRUD INICIO =====

    // CREAR ADOPTANTE
    public AdopterEntity createAdopter(AdopterEntity adopter) {
        return adopterRepository.save(adopter);
    }

    // OBTENER POR ID
    public AdopterEntity getAdopter(Long id) throws EntityNotFoundException {
        return adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"));
    }

    // LISTAR TODOS
    public List<AdopterEntity> getAdopters() {
        return adopterRepository.findAll();
    }

    // ACTUALIZAR
    public AdopterEntity updateAdopter(Long id, AdopterEntity newAdopter) throws EntityNotFoundException {

        AdopterEntity adopter = getAdopter(id);

        // ===== DATOS CLIENT =====
        adopter.setClientName(newAdopter.getClientName());
        adopter.setClientPhone(newAdopter.getClientPhone());
        adopter.setClientEmail(newAdopter.getClientEmail());

        // ===== DATOS PROPIOS =====
        adopter.setHasChildren(newAdopter.getHasChildren());
        adopter.setHasPets(newAdopter.getHasPets());

        return adopterRepository.save(adopter);
    }

    // ELIMINAR
    public void deleteAdopter(Long id) throws EntityNotFoundException {
        AdopterEntity adopter = getAdopter(id);
        adopterRepository.delete(adopter);
    }

    // ===== MÉTODOS CRUD FIN =====

}