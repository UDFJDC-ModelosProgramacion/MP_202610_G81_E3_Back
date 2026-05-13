package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class BackgroundEntity extends BaseEntity{
    private LocalDate date;
    private String description;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;
}
