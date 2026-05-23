package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.dto.PetDetailDTO;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PetDetailDTO> findAll() {
        List<PetEntity> pets = petService.getPets();
        return pets.stream().map(this::toPetDetailDTO).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PetDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        return toPetDetailDTO(petService.getPet(id));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PetDTO create(@RequestBody PetDTO petDTO) throws IllegalOperationException, EntityNotFoundException {
        PetEntity petEntity = petService.createPet(modelMapper.map(petDTO, PetEntity.class));
        return modelMapper.map(petEntity, PetDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PetDTO update(@PathVariable Long id, @RequestBody PetDTO petDTO) throws EntityNotFoundException {
        PetEntity petEntity = petService.updatePet(id, modelMapper.map(petDTO, PetEntity.class));
        return modelMapper.map(petEntity, PetDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetDetailDTO>> searchPets(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) List<String> filter) {
        List<PetEntity> resultado = petService.searchPets(keyword, filter);
        List<PetDetailDTO> dtos = resultado.stream().map(this::toPetDetailDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private PetDetailDTO toPetDetailDTO(PetEntity pet) {
        PetDetailDTO dto = modelMapper.map(pet, PetDetailDTO.class);
        if (pet.getShelter() != null) {
            dto.setShelterName(pet.getShelter().getName());
        }
        return dto;
    }
}