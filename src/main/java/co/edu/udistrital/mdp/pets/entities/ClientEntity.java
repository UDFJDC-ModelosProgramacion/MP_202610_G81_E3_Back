package co.edu.udistrital.mdp.pets.entities;

// ===== IMPORTS INICIO =====
import jakarta.persistence.Entity;
import lombok.Data;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Data
@Entity
public class ClientEntity extends BaseEntity {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== ATRIBUTOS INICIO =====
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    // ===== ATRIBUTOS FIN =====


    // ===== RELACIONES INICIO =====
    // (Sin relaciones - modelo simplificado)
    // ===== RELACIONES FIN =====

}