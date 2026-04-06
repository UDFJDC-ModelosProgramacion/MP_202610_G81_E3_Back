package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ShelterEventDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate date;
}