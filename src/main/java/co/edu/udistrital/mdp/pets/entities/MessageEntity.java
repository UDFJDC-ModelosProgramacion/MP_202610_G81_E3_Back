package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class MessageEntity extends BaseEntity{
    String author;
    String messageContent;
    LocalDate date;
}