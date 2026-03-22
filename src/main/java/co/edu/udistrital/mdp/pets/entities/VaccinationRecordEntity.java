package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class VaccinationRecordEntity extends BaseEntity{
    private String vaccineName;
    private LocalDate vaccineDate;
    private LocalDate nextDosesDate;
    @ManyToOne
    private PetEntity pet;

}
