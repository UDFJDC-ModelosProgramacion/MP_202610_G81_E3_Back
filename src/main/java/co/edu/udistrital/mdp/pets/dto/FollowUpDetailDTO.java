package co.edu.udistrital.mdp.pets.dto;

import java.util.List;
import lombok.Data;

@Data
public class FollowUpDetailDTO extends FollowUpDTO {
    // Asociacion cardinalidad 1 con Pet 
    private PetDTO pet;
    // Asociacion cardinalidad 1 con Veterinary 
    private VeterinaryDTO veterinary;
    // Asociacion OneToMany con VetVisit
    private List<VetVisitDTO> vetVisits;
}