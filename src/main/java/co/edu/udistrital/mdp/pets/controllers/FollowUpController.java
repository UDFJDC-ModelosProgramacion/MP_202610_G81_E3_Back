package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.FollowUpDTO;
import co.edu.udistrital.mdp.pets.dto.FollowUpDetailDTO;
import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.FollowUpService;

@RestController
@RequestMapping("/api/followups")
public class FollowUpController {

    @Autowired
    private FollowUpService followUpService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FollowUpDTO create(@RequestBody FollowUpDTO followUpDTO) throws IllegalOperationException {
        FollowUpEntity entity = modelMapper.map(followUpDTO, FollowUpEntity.class);
        return modelMapper.map(followUpService.createFollowUp(entity), FollowUpDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FollowUpDetailDTO> findAll() {
        List<FollowUpEntity> followUps = followUpService.getFollowUps();
        return modelMapper.map(followUps, new TypeToken<List<FollowUpDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FollowUpDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        FollowUpEntity entity = followUpService.getFollowUp(id);
        return modelMapper.map(entity, FollowUpDetailDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FollowUpDTO update(@PathVariable Long id, @RequestBody FollowUpDTO followUpDTO)
            throws EntityNotFoundException, IllegalOperationException {
        FollowUpEntity entity = modelMapper.map(followUpDTO, FollowUpEntity.class);
        return modelMapper.map(followUpService.updateFollowUp(id, entity), FollowUpDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        followUpService.deleteFollowUp(id);
    }
}