package co.edu.udistrital.mdp.pets.entities;

// ===== IMPORTS INICIO =====
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
// ===== IMPORTS FIN =====

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ClientEntity extends BaseEntity {

    // ===== ATRIBUTOS INICIO =====
    private String clientName;
    private String clientPhone;
    private String clientEmail;
    private String password; // <-- AGREGAMOS ESTE CAMPO PARA EL LOGIN
    // ===== ATRIBUTOS FIN =====

    // ===== RELACIONES INICIO =====
    // (Sin relaciones - modelo simplificado)
    // ===== RELACIONES FIN =====
}