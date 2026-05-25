package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class MediaFileEntity extends BaseEntity {

    private String url;
    private MediaFileType mediaFileType;

    @PodamExclude
    @ManyToOne
    private PetEntity pet;

    @PodamExclude
    @ManyToOne
    private ShelterEntity shelter;
}
