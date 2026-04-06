package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ShelterEventDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;

@RestController
@RequestMapping("/events")
public class ShelterEventController {

    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterEventDTO> findAll() {
        List<ShelterEventEntity> list = shelterEventService.getEvents();
        return modelMapper.map(list, new TypeToken<List<ShelterEventDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ShelterEventEntity entity = shelterEventService.getEvent(id);
        return modelMapper.map(entity, ShelterEventDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterEventDTO create(@RequestBody ShelterEventDTO dto)
            throws IllegalOperationException, EntityNotFoundException {

        ShelterEventEntity entity = shelterEventService.createShelterEvent(
                modelMapper.map(dto, ShelterEventEntity.class));

        return modelMapper.map(entity, ShelterEventDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterEventDTO update(@PathVariable Long id, @RequestBody ShelterEventDTO dto)
            throws EntityNotFoundException, IllegalOperationException {

        ShelterEventEntity entity = shelterEventService.updateShelterEventEntity(
                id, modelMapper.map(dto, ShelterEventEntity.class));

        return modelMapper.map(entity, ShelterEventDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {

        shelterEventService.deleteShelterEvent(id);
    }
}