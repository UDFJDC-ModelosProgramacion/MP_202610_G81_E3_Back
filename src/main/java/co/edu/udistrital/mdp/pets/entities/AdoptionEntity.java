package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Entity
@Data

public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;
    private String status;
    
    @PodamExclude
    @ManyToOne
    private AdopterEntity adopter;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;

    @PodamExclude
    @OneToMany(mappedBy = "adoption")
    private List<TrialPeriodEntity> trialPeriods = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "adoption")
    private List<AdoptionDefEntity> adoptionDefs = new ArrayList<>();

}
