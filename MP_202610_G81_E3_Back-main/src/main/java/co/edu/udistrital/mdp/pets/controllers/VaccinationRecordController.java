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

import co.edu.udistrital.mdp.pets.dto.VaccinationRecordDTO;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetVaccinationRecordService;
import co.edu.udistrital.mdp.pets.services.VaccinationRecordService;

@RestController
@RequestMapping("/pets")
public class VaccinationRecordController {

    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private PetVaccinationRecordService petVaccinationRecordService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{petId}/vaccinationRecords")
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccinationRecordDTO> getVaccinationRecords(@PathVariable Long petId) throws EntityNotFoundException {
        List<VaccinationRecordEntity> vaccinationRecords = petVaccinationRecordService.getVaccinationRecords(petId);
        return modelMapper.map(vaccinationRecords, new TypeToken<List<VaccinationRecordDTO>>() {
        }.getType());
    }

    @PostMapping(value = "/{petId}/vaccinationRecords")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VaccinationRecordDTO createVaccinationRecord(@PathVariable Long petId, @RequestBody VaccinationRecordDTO vaccinationRecordDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VaccinationRecordEntity vaccinationRecordEntity = modelMapper.map(vaccinationRecordDTO, VaccinationRecordEntity.class);
        VaccinationRecordEntity newVaccinationRecord = vaccinationRecordService.createVaccinationRecord(vaccinationRecordEntity); //crea el registro
        petVaccinationRecordService.addVaccinationRecord(petId, newVaccinationRecord.getId()); // se asocia el registro a la mascota
        return modelMapper.map(newVaccinationRecord, VaccinationRecordDTO.class);
    }

    @GetMapping(value = "/{petId}/vaccinationRecords/{vaccinationRecordId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccinationRecordDTO getVaccinationRecord(@PathVariable Long petId, @PathVariable Long vaccinationRecordId)
            throws EntityNotFoundException {
        VaccinationRecordEntity vaccinationRecordEntity = vaccinationRecordService.getVaccinationRecord(petId, vaccinationRecordId);
        return modelMapper.map(vaccinationRecordEntity, VaccinationRecordDTO.class);
    }

    @PutMapping(value = "/vaccinationRecords/{vaccinationRecordId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccinationRecordDTO updateVaccinationRecord(@PathVariable Long vaccinationRecordId,
            @RequestBody VaccinationRecordDTO vaccinationRecordDTO) throws EntityNotFoundException {
        VaccinationRecordEntity vaccinationRecordEntity = modelMapper.map(vaccinationRecordDTO, VaccinationRecordEntity.class);
        VaccinationRecordEntity newVaccinationRecord = vaccinationRecordService.update(vaccinationRecordId, vaccinationRecordEntity);
        return modelMapper.map(newVaccinationRecord, VaccinationRecordDTO.class);
    }

    @DeleteMapping(value = "/vaccinationRecords/{vaccinationRecordId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteVaccinationRecord(@PathVariable Long vaccinationRecordId)
            throws EntityNotFoundException, IllegalOperationException {
        vaccinationRecordService.deleteVaccinationRecord(vaccinationRecordId);
    }

}
