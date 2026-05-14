package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.ArriveToShelter;
import co.edu.udistrital.mdp.pets.entities.PetState;
import lombok.Data;

@Data
public class PetDTO {
    private Long id;  // ← agrega esta línea
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String sex;
    private Float size;
    private String temperament;
    private LocalDate arriveToShelterDate;
    private String specificRequirements;
    private PetState petState;
    private ArriveToShelter arriveToShelter;

    //falta folloupDTO
    //falta un shelter
}
