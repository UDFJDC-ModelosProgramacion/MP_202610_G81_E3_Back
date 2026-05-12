package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class AdopterDTO {

    // ===== ATRIBUTOS INICIO =====
    private Long id;  // ← línea nueva

    // ----- Datos de Client -----
    private String clientName;
    private String clientPhone;
    private String clientEmail;

    // ----- Datos propios -----
    private Boolean hasChildren;
    private Boolean hasPets;

    // ===== ATRIBUTOS FIN =====

}