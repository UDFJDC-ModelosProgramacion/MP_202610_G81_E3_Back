// ===== IMPORTS INICIO =====
package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import co.edu.udistrital.mdp.pets.services.AdoptionService;
import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    // ===== ATRIBUTOS INICIO =====
    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private ModelMapper modelMapper;
    // ===== ATRIBUTOS FIN =====


    // ===== MÉTODOS INICIO =====

    // GET /adoptions → lista todas las adopciones
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdoptionDTO> findAll() {
        List<AdoptionEntity> adoptions = adoptionService.getAdoptions();
        return modelMapper.map(adoptions, new TypeToken<List<AdoptionDTO>>() {}.getType());
    }

    // GET /adoptions/{id} → traer una adopción por ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionDTO findById(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionEntity adoption = adoptionService.getAdoption(id);
        return modelMapper.map(adoption, AdoptionDTO.class);
    }

    // POST /adoptions → crear una nueva adopción
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionDTO create(@RequestBody AdoptionDTO adoptionDTO) throws EntityNotFoundException {

        AdoptionEntity adoptionEntity = modelMapper.map(adoptionDTO, AdoptionEntity.class);

        AdoptionEntity savedAdoption = adoptionService.createAdoption(
                adoptionDTO.getAdopterId(),
                adoptionDTO.getPetId(),
                adoptionEntity
        );

        return modelMapper.map(savedAdoption, AdoptionDTO.class);
    }

    // PUT /adoptions/{id} → actualizar adopción (solo status y trialEndDate)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionDTO update(@PathVariable Long id, @RequestBody AdoptionDTO adoptionDTO) throws EntityNotFoundException {

        AdoptionEntity adoptionEntity = modelMapper.map(adoptionDTO, AdoptionEntity.class);

        AdoptionEntity updatedAdoption = adoptionService.updateAdoption(id, adoptionEntity);

        return modelMapper.map(updatedAdoption, AdoptionDTO.class);
    }

    // DELETE /adoptions/{id} → eliminar adopción
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        adoptionService.deleteAdoption(id);
    }

    // ===== MÉTODOS FIN =====

}
// ===== DEFINICIÓN DE CLASE FIN =====