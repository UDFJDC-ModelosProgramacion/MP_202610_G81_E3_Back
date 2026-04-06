package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class FollowUpEntity extends BaseEntity {

    private String observation;
    private LocalDate visitDate;

    @PodamExclude
    @ManyToOne
    private VeterinaryEntity veterinary;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;

    @PodamExclude
    @OneToMany(mappedBy = "followUp")
    private List<VetVisitEntity> vetVisits = new ArrayList<>();
}