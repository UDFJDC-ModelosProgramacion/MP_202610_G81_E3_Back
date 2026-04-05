package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PetDetailDTO extends PetDTO{
    private List<MedicalEventDTO> medicalEvents=new ArrayList<>();
    private List<VaccinationRecordDTO> vaccinationRecords=new ArrayList<>();
    private List<MediaFileDTO> photographes=new ArrayList<>();
    private List<BackgroundDTO>backgrounds=new ArrayList<>();

    //faltan las adoptions
}
