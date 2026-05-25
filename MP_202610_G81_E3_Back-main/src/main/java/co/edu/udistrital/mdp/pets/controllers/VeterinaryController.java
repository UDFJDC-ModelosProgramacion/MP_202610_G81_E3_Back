package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.VeterinaryDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinaryDetailDTO;
import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VeterinaryService;

@RestController
@RequestMapping("/api/veterinaries")
public class VeterinaryController {

    @Autowired
    private VeterinaryService veterinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinaryDTO create(@RequestBody VeterinaryDTO veterinaryDTO) throws IllegalOperationException {
        VeterinaryEntity entity = modelMapper.map(veterinaryDTO, VeterinaryEntity.class);
        return modelMapper.map(veterinaryService.createVeterinary(entity), VeterinaryDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VeterinaryDetailDTO> findAll() {
        List<VeterinaryEntity> veterinaries = veterinaryService.getVeterinaries();
        return modelMapper.map(veterinaries, new TypeToken<List<VeterinaryDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VeterinaryDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        VeterinaryEntity entity = veterinaryService.getVeterinary(id);
        return modelMapper.map(entity, VeterinaryDetailDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VeterinaryDTO update(@PathVariable Long id, @RequestBody VeterinaryDTO veterinaryDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinaryEntity entity = modelMapper.map(veterinaryDTO, VeterinaryEntity.class);
        return modelMapper.map(veterinaryService.updateVeterinary(id, entity), VeterinaryDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        veterinaryService.deleteVeterinary(id);
    }
}