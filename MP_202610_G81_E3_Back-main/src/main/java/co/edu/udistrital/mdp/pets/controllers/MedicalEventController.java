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

import co.edu.udistrital.mdp.pets.dto.MedicalEventDTO;
import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicalEventService;
import co.edu.udistrital.mdp.pets.services.PetMedicalEventService;

@RestController
@RequestMapping("/pets")
public class MedicalEventController {

    @Autowired
    private MedicalEventService medicalEventService;
    @Autowired
    private PetMedicalEventService petMedicalEventService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value="/{petId}/medicalEvents")
    @ResponseStatus(code=HttpStatus.OK)
    public List<MedicalEventDTO> getMedicalEvents(@PathVariable Long petId) throws EntityNotFoundException {
        List<MedicalEventEntity> medicalEvents=petMedicalEventService.getMedicalEvents(petId);
        return modelMapper.map(medicalEvents, new TypeToken<List<MedicalEventDTO>>(){
        }.getType());
    }

    @PostMapping(value = "/{petId}/medicalEvents")
	@ResponseStatus(code = HttpStatus.CREATED)
	public MedicalEventDTO createMedicalEvent(@PathVariable Long petId, @RequestBody MedicalEventDTO medicalEventDTO)
			throws EntityNotFoundException, IllegalOperationException{
		MedicalEventEntity medicalEventEntity = modelMapper.map(medicalEventDTO, MedicalEventEntity.class);
		MedicalEventEntity newMedicalEvent = medicalEventService.createMedicalEvent(medicalEventEntity);//se crea el evento medico
        petMedicalEventService.addMedicalEvent(newMedicalEvent.getId(), petId); // se asocia el evento medico a la mascota
		return modelMapper.map(newMedicalEvent, MedicalEventDTO.class);
	}

    @GetMapping(value = "/{petId}/medicalEvents/{medicalEventId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MedicalEventDTO getMedicalEvent(@PathVariable Long petId, @PathVariable Long medicalEventId)
			throws EntityNotFoundException {
		MedicalEventEntity medicalEventEntity = medicalEventService.getMedicalEvent(petId, medicalEventId);
		return modelMapper.map(medicalEventEntity, MedicalEventDTO.class);
	}


    @PutMapping(value = "/medicalEvents/{medicalEventId}")
	@ResponseStatus(code = HttpStatus.OK)
	public MedicalEventDTO updateMedicalEvent(@PathVariable Long medicalEventId,
			@RequestBody MedicalEventDTO medicalEventDTO) throws EntityNotFoundException {
		MedicalEventEntity medicalEventEntity = modelMapper.map(medicalEventDTO, MedicalEventEntity.class);
		MedicalEventEntity newMedicalEvent = medicalEventService.updateMedicalEventEntity(medicalEventId, medicalEventEntity);
		return modelMapper.map(newMedicalEvent, MedicalEventDTO.class);
	}

    @DeleteMapping(value = "/medicalEvents/{medicalEventId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteMedicalEvent(@PathVariable Long medicalEventId)
			throws EntityNotFoundException, IllegalOperationException {
		medicalEventService.deleteMedicalEvent(medicalEventId);
	}


}
