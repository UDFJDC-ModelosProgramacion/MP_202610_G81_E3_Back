package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PetDTO {
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String sex;
    private Float size;
    private String temperament;
    private LocalDate arriveToShelter;
    private String specificRequirements;
}
