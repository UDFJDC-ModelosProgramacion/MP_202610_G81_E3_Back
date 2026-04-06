package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MedicalEventDTO {
    private String description;
    private LocalDate date;
    private PetDTO pet;
}
