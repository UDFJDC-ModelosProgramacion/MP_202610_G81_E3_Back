package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class ShelterDTO {
    // Se agrega id para editar por id.
    private Long id;
    
    private String name;
    private String city;
    private String address;
    private String email;
    private String image;
}