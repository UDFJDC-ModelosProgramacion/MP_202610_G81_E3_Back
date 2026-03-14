package co.edu.udistrital.mdp.pets.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class ShelterEntity extends BaseEntity {

    private String name;
    private String city;
    private String address;

    @PodamExclude
    @OneToMany(mappedBy="shelter")
    private List<MediaFileEntity> mediaFiles;   //this includes photographes and videos

    //Asociations with Events and Messages.
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ShelterEventEntity> events;

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<MessageEntity> messages;

}
