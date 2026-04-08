package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class VeterinaryDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialty;
    private String availability;
}