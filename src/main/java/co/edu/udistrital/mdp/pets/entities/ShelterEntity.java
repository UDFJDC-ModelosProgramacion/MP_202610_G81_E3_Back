package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
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
    private String email;

    @PodamExclude
    @OneToMany(mappedBy="shelter")
    private List<MediaFileEntity> mediaFiles=new ArrayList<>();   //this includes photographes and videos

    //Asociations with Events and Messages.
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ShelterEventEntity> events=new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<MessageEntity> messages=new ArrayList<>();

    //Agregacion de la relacion entre veterinarios y shelter.
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<VeterinaryEntity> veterinarians = new ArrayList<>();

    //Agregacion de pet.
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<PetEntity> pets = new ArrayList<>();
}
