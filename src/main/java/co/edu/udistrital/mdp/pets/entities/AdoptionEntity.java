package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data

public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;
    private Boolean status;
    
    @ManyToOne
    private ClientEntity client;

}
