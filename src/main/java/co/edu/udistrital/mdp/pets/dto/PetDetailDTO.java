package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) 
public class PetDetailDTO extends PetDTO{
    private List<MedicalEventDTO> medicalEvents=new ArrayList<>();
    private List<VaccinationRecordDTO> vaccinationRecords=new ArrayList<>();
    private List<MediaFileDTO> photographs=new ArrayList<>();
    private List<BackgroundDTO>backgrounds=new ArrayList<>();
    private String shelterName;

    //faltan las adoptions
}
