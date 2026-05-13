package co.edu.udistrital.mdp.pets.dto;
import co.edu.udistrital.mdp.pets.entities.MediaFileType;
import lombok.Data;

@Data
public class MediaFileDTO {
    private String url;
    private MediaFileType mediaFileType;
    private PetDTO pet;
    //falta el shelterDTO
}
