package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class FollowUpEntity extends BaseEntity {

    private String observation;
    private LocalDate visitDate; // Reemplaza @Temporal + Date (obsoleto en Jakarta)

    @PodamExclude
    @ManyToOne
    private VeterinaryEntity veterinary;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;
}