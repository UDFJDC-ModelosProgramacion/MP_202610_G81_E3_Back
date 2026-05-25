package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import co.edu.udistrital.mdp.pets.services.AdopterService;
import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

@RestController
@RequestMapping("/adopters")
@CrossOrigin(origins = "http://localhost:5173") // Ojo: Permite que React se conecte sin bloqueos de CORS
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @Autowired
    private ModelMapper modelMapper;

    // GET /adopters → listar todos los adoptantes
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdopterDTO> findAll() {
        List<AdopterEntity> adopters = adopterService.getAdopters();
        return modelMapper.map(adopters, new TypeToken<List<AdopterDTO>>() {
        }.getType());
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
}