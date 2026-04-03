package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class PetEntity extends BaseEntity {

    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String sex;
    private Float size;
    private String temperament;
    private LocalDate arriveToShelterDate;
    private String specificRequirements;
    private PetState petState;
    private ArriveToShelter arriveToShelter;

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MedicalEventEntity> medicalEvents=new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<VaccinationRecordEntity> vaccinationRecords=new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MediaFileEntity> photographes=new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy="pet")
    private List<BackgroundEntity> backgrounds=new ArrayList<>();
    // se agrega la relacion de agregacion debil que se ve en el diagrama.
    //es la relacionde petentity a adoptionentity
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<AdoptionEntity> adoptions;

}
