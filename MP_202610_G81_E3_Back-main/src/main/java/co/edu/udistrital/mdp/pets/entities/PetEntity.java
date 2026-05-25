package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
    private String size;
    private String temperament;
    private LocalDate arriveToShelterDate;
    private String specificRequirements;
    private String requiredSpace;

    @Column(length = 1000000)
    private String image;

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<VaccinationRecordEntity> vaccinationRecords = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")

    private List<MediaFileEntity> photographs = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<BackgroundEntity> backgrounds = new ArrayList<>();
    // se agrega la relacion de agregacion debil que se ve en el diagrama.
    // es la relacionde petentity a adoptionentity
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<AdoptionEntity> adoptions;

    // Se agrega la relacion de follow up.
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<FollowUpEntity> followUps = new ArrayList<>();

    // Se agrega la relacion de Shelter.
    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;

    // Enum implementado.
    @Enumerated(EnumType.STRING)
    private PetState petState;

    @Enumerated(EnumType.STRING)
    private ArriveToShelter arriveToShelter;
}
