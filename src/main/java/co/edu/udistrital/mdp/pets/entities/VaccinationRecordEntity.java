package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class VaccinationRecordEntity extends BaseEntity{
    private String vaccineName;
    private Date vaccineDate;
    private Date nextDosesDate;
    @ManyToOne
    private PetEntity pet;

}
