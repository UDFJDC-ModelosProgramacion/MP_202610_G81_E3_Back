package co.edu.udistrital.mdp.pets.controllers;

// ===== IMPORTS INICIO =====
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

import co.edu.udistrital.mdp.pets.services.AdopterService;
import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@RestController
@RequestMapping("/adopters")
public class AdopterController {

    // ===== ATRIBUTOS INICIO =====
    @Autowired
    private AdopterService adopterService;

    @Autowired
    private ModelMapper modelMapper;
    // ===== ATRIBUTOS FIN =====


    // ===== MÉTODOS INICIO =====

    // GET /adopters → listar todos los adoptantes
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdopterDTO> findAll() {
        List<AdopterEntity> adopters = adopterService.getAdopters();
        return modelMapper.map(adopters, new TypeToken<List<AdopterDTO>>() {}.getType());
    }

    // GET /adopters/{id} → traer adoptante por ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdopterDTO findById(@PathVariable Long id) throws EntityNotFoundException {
        AdopterEntity adopter = adopterService.getAdopter(id);
        return modelMapper.map(adopter, AdopterDTO.class);
    }

    // POST /adopters → crear adoptante
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdopterDTO create(@RequestBody AdopterDTO adopterDTO) {

        AdopterEntity adopterEntity = modelMapper.map(adopterDTO, AdopterEntity.class);
        AdopterEntity savedAdopter = adopterService.createAdopter(adopterEntity);

        return modelMapper.map(savedAdopter, AdopterDTO.class);
    }

    // PUT /adopters/{id} → actualizar adoptante
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdopterDTO update(@PathVariable Long id, @RequestBody AdopterDTO adopterDTO) throws EntityNotFoundException {

        AdopterEntity adopterEntity = modelMapper.map(adopterDTO, AdopterEntity.class);
        AdopterEntity updatedAdopter = adopterService.updateAdopter(id, adopterEntity);

        return modelMapper.map(updatedAdopter, AdopterDTO.class);
    }

    // DELETE /adopters/{id} → eliminar adoptante
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        adopterService.deleteAdopter(id);
    }

    // ===== MÉTODOS FIN =====

}
// ===== DEFINICIÓN DE CLASE FIN =====