package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity

public class ShelterEventEntity extends BaseEntity {

    LocalDate fecha;
    String name;
    String description;

}
