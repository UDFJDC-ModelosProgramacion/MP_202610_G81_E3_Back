package co.edu.udistrital.mdp.pets.entities;


import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class MedicalEventEntity extends BaseEntity{
    public String descripcion;
    public Date fecha;
    @ManyToOne
    public PetEntity pet;
}
