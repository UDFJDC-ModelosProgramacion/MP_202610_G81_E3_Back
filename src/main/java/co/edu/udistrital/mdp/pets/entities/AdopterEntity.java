package co.edu.udistrital.mdp.pets.entities;

// ===== IMPORTS INICIO =====
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnore;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdopterEntity extends ClientEntity {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== ATRIBUTOS INICIO =====
    private Boolean hasChildren;
    private Boolean hasPets;
    // ===== ATRIBUTOS FIN =====


    // ===== RELACIONES INICIO =====
    @JsonIgnore
    @OneToMany(mappedBy = "adopter")
    private List<AdoptionEntity> adoptions = new ArrayList<>();
    // ===== RELACIONES FIN =====

}