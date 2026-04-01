package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.ManyToOne;

@Data
@Entity

public class NotificationEntity extends BaseEntity {
    
    private String message;
    private LocalDate date;

    @ManyToOne
    private ClientEntity client;

}
