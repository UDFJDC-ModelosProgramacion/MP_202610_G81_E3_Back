package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.VetVisitDTO;
import co.edu.udistrital.mdp.pets.entities.VetVisitEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VetVisitService;

@RestController
@RequestMapping("/pets")
public class VetVisitController {

    @Autowired
    private VetVisitService vetVisitService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VetVisitDTO create(@RequestBody VetVisitDTO vetVisitDTO) throws IllegalOperationException {
        VetVisitEntity entity = modelMapper.map(vetVisitDTO, VetVisitEntity.class);
        return modelMapper.map(vetVisitService.createVetVisit(entity), VetVisitDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VetVisitDetailDTO> findAll() {
        List<VetVisitEntity> vetVisits = vetVisitService.getVetVisits();
        return modelMapper.map(vetVisits, new TypeToken<List<VetVisitDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VetVisitDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        VetVisitEntity entity = vetVisitService.getVetVisit(id);
        return modelMapper.map(entity, VetVisitDetailDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VetVisitDTO update(@PathVariable Long id, @RequestBody VetVisitDTO vetVisitDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VetVisitEntity entity = modelMapper.map(vetVisitDTO, VetVisitEntity.class);
        return modelMapper.map(vetVisitService.updateVetVisit(id, entity), VetVisitDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        vetVisitService.deleteVetVisit(id);
    }
}