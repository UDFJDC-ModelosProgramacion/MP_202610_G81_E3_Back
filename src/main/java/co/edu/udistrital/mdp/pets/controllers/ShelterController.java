package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ShelterDTO;
import co.edu.udistrital.mdp.pets.dto.ShelterDetailDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterDetailDTO> findAll() {
        List<ShelterEntity> list = shelterService.getShelters();
        return modelMapper.map(list, new TypeToken<List<ShelterDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ShelterEntity entity = shelterService.getShelter(id);
        return modelMapper.map(entity, ShelterDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterDTO create(@RequestBody ShelterDTO dto)
            throws IllegalOperationException, EntityNotFoundException {

        ShelterEntity entity = shelterService.createShelter(
                modelMapper.map(dto, ShelterEntity.class));

        return modelMapper.map(entity, ShelterDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterDTO update(@PathVariable Long id, @RequestBody ShelterDTO dto)
            throws EntityNotFoundException, IllegalOperationException {

        ShelterEntity entity = shelterService.updateShelter(
                id, modelMapper.map(dto, ShelterEntity.class));

        return modelMapper.map(entity, ShelterDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {

        shelterService.deleteShelter(id);
    }

    @GetMapping("/search")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterDetailDTO> search(@RequestParam String keyword) {
        List<ShelterEntity> list = shelterService.searchShelters(keyword);

        return modelMapper.map(
            list,
            new TypeToken<List<ShelterDetailDTO>>() {}.getType()
        );
    }
}