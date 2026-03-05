package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Data
@Entity

public class PetEntity extends BaseEntity{
    
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String sex;
    private Float size;
    private String temperament;
    private Date arriveToShelter;
    private String specificRequirements;

    @PodamExclude
    @OneToMany(mappedBy="pet")
    private List<MedicalEventEntity>medicalEvents;
    

}
