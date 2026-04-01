package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdoptionDefEntity extends BaseEntity  {

    @ManyToOne
    private AdoptionEntity adoption;

    
}