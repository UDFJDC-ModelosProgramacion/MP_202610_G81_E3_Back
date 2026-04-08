package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FollowUpDTO {
    private Long id;
    private String observation;
    private LocalDate visitDate;
}