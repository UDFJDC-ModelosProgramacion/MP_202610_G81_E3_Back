package co.edu.udistrital.mdp.pets.dto;

import java.util.List;

import lombok.Data;

@Data
public class ShelterDetailDTO extends ShelterDTO {

    private List<PetDTO> pets;

    private List<MediaFileDTO> mediaFiles;

    private List<ShelterEventDTO> events;

    private List<MessageDTO> messages;
}