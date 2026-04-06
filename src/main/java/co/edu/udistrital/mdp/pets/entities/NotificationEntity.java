package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class NotificationEntity extends BaseEntity {
    
    private String message;
    private LocalDate date;

    @PodamExclude
    @ManyToOne
    private ClientEntity client;

}
