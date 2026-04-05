package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BackgroundDTO {
    private LocalDate date;
    private String description;
    private PetDTO pet;
}
