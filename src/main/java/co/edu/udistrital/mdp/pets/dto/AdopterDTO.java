package co.edu.udistrital.mdp.pets.dto;

// ===== IMPORTS INICIO =====
import lombok.Data;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Data
public class AdopterDTO {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== ATRIBUTOS INICIO =====

    // ----- Datos de Client -----
    private String clientName;
    private String clientPhone;
    private String clientEmail;

    // ----- Datos propios -----
    private Boolean hasChildren;
    private Boolean hasPets;

    // ===== ATRIBUTOS FIN =====

}