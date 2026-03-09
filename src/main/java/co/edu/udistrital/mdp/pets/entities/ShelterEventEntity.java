package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.time.LocalDate;

@Data
@Entity

public class ShelterEventEntity extends BaseEntity {

    LocalDate fecha;
    String name;
    String description;

    //Asociation with Shelter Entity, one shelter has many events.
    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;

}
