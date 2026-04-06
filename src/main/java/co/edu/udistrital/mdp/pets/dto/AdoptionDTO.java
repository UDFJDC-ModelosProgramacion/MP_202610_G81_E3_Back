package co.edu.udistrital.mdp.pets.dto;

// ===== IMPORTS INICIO =====
import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionStatus;

import lombok.Data;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Data
public class AdoptionDTO {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== ATRIBUTOS INICIO =====
    private LocalDate adoptionDate;
    
    private LocalDate trialStartDate;
    private LocalDate trialEndDate;

    private AdoptionStatus status;

    private Long adopterId;
    private Long petId;
    // ===== ATRIBUTOS FIN =====

}