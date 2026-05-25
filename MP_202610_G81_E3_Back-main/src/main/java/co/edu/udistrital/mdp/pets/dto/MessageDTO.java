package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MessageDTO {

    private Long id;
    private String author;
    private String messageContent;
    private LocalDate date;
}