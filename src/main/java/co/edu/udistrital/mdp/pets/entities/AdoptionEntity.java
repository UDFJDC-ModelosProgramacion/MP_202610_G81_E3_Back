package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import java.time.LocalDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Data
public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;

    private LocalDate trialStartDate;
    private LocalDate trialEndDate;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;
    
    @ManyToOne
    private AdopterEntity adopter;

    @ManyToOne
    private PetEntity pet;

    @OneToOne
    private ReturnPetEntity returnPet;

    @OneToOne
private FollowUpEntity followUp;


}