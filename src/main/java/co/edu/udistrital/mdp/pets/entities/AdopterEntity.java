package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdopterEntity extends ClientEntity {

    private Boolean hasChildren;
    private Boolean hasPets;

    @OneToMany(mappedBy = "adopter")
    private List<AdoptionEntity> adoptions = new ArrayList<>();
}