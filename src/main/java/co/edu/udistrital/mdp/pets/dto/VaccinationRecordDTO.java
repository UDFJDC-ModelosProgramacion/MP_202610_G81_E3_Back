package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VaccinationRecordDTO {
    private String vaccineName;
    private LocalDate vaccineDate;
    private LocalDate nextDosesDate;
}
