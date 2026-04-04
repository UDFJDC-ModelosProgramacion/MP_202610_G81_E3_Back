package co.edu.udistrital.mdp.pets.entities;

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

    @PodamExclude
    @ManyToOne
    private VeterinaryEntity veterinary;

    private PetEntity pet;

    @PodamExclude
    @OneToMany(mappedBy="followUp")
    private List<VetVisitEntity> vetVisits=new ArrayList<>();
}