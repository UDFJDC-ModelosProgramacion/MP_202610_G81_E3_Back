package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class ReturnPetEntity extends BaseEntity {

    private String reason;
    private LocalDate returnDate;

    //Relacion con adoption.
    @OneToOne
    private AdoptionEntity adoption;
}