package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.BackgroundDTO;
import co.edu.udistrital.mdp.pets.entities.BackgroundEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.BackgroundService;
import co.edu.udistrital.mdp.pets.services.PetBackgroundService;

@RestController
@RequestMapping("/pets")
public class BackgroundController {

    @Autowired
    private BackgroundService backgroundService;

    @Autowired
    private PetBackgroundService petBackgroundService;

    @Autowired
    private ModelMapper modelMapper;

    // GET todos los antecedentes de una mascota
    @GetMapping(value = "/{petId}/backgrounds")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BackgroundDTO> getBackgrounds(@PathVariable Long petId) throws EntityNotFoundException {
        List<BackgroundEntity> backgrounds = petBackgroundService.getBackgrounds(petId);
        return modelMapper.map(backgrounds, new TypeToken<List<BackgroundDTO>>() {
        }.getType());
    }

    // POST crear antecedente y asociarlo a una mascota
    @PostMapping(value = "/{petId}/backgrounds")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BackgroundDTO createBackground(@PathVariable Long petId, @RequestBody BackgroundDTO backgroundDTO)
            throws EntityNotFoundException, IllegalOperationException {
        BackgroundEntity backgroundEntity = modelMapper.map(backgroundDTO, BackgroundEntity.class);
        BackgroundEntity newBackground = backgroundService.createBackground(backgroundEntity);
        petBackgroundService.addBackground(newBackground.getId(), petId);
        return modelMapper.map(newBackground, BackgroundDTO.class);
    }

    // GET un antecedente específico de una mascota
    @GetMapping(value = "/{petId}/backgrounds/{backgroundId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BackgroundDTO getBackground(@PathVariable Long petId, @PathVariable Long backgroundId)
            throws EntityNotFoundException {
        BackgroundEntity backgroundEntity = backgroundService.getBackground(petId, backgroundId);
        return modelMapper.map(backgroundEntity, BackgroundDTO.class);
    }

    // PUT actualizar antecedente
    @PutMapping(value = "/backgrounds/{backgroundId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BackgroundDTO updateBackground(@PathVariable Long backgroundId,
            @RequestBody BackgroundDTO backgroundDTO) throws EntityNotFoundException {
        BackgroundEntity backgroundEntity = modelMapper.map(backgroundDTO, BackgroundEntity.class);
        BackgroundEntity updatedBackground = backgroundService.updateBackground(backgroundId, backgroundEntity);
        return modelMapper.map(updatedBackground, BackgroundDTO.class);
    }

    // DELETE eliminar antecedente
    @DeleteMapping(value = "/backgrounds/{backgroundId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBackground(@PathVariable Long backgroundId)
            throws EntityNotFoundException, IllegalOperationException {
        backgroundService.deleteBackground(backgroundId);
    }
}