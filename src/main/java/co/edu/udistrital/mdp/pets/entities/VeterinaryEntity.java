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
public class VeterinaryEntity extends BaseEntity {

    private String name;
    private String email;
    private String phone;
    private String specialty;
    private String availability;

    @PodamExclude
    @OneToMany(mappedBy = "veterinary")
    private List<FollowUpEntity> followUps = new ArrayList<>();

    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;
}