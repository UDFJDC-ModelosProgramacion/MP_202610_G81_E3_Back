package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class ShelterEntity extends BaseEntity {

    String name;
    String city;
    String address;
    String pictures;
    String videos;

}
