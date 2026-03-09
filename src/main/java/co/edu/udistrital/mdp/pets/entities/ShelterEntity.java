package co.edu.udistrital.mdp.pets.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class ShelterEntity extends BaseEntity {

    String name;
    String city;
    String address;
    String pictures;
    String videos;

    //Asociations with Events and Messages.
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ShelterEventEntity> events;

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<MessageEntity> messages;

}
