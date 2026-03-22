package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class MedicalEventEntity extends BaseEntity{
    public String description;
    public LocalDate date;

    @PodamExclude
    @ManyToOne
    public PetEntity pet;
}
