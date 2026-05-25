package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReturnPetDTO {
    private Long id;
    private String reason;
    private LocalDate returnDate;
    private Long adoptionId;  // ← agrega esta línea

}