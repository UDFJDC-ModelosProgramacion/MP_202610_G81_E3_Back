package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class MessageEntity extends BaseEntity{
    String author;
    String messageContent;
    LocalDate date;
    //Relacion con shelter.
    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;

    //Relacion con cliente.
    @PodamExclude
    @ManyToOne
    private ClientEntity client;
}