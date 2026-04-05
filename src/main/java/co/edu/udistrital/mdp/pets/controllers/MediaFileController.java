package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MediaFileDTO;
import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MediaFileService;
import co.edu.udistrital.mdp.pets.services.PetMediaFileService;

@RestController
@RequestMapping("/pets")
public class MediaFileController {

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private PetMediaFileService petMediaFileService;

    @Autowired
    private ModelMapper modelMapper;

    // GET todos los archivos de una mascota
    @GetMapping(value = "/{petId}/mediaFiles")
    @ResponseStatus(code = HttpStatus.OK)
    public List<MediaFileDTO> getMediaFiles(@PathVariable Long petId) throws EntityNotFoundException {
        List<MediaFileEntity> mediaFiles = petMediaFileService.getPhotographs(petId);
        return modelMapper.map(mediaFiles, new TypeToken<List<MediaFileDTO>>() {
        }.getType());
    }

    // POST crear archivo y asociarlo a una mascota
    @PostMapping(value = "/{petId}/mediaFiles")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MediaFileDTO createMediaFile(@PathVariable Long petId, @RequestBody MediaFileDTO mediaFileDTO)
            throws EntityNotFoundException, IllegalOperationException {
        MediaFileEntity mediaFileEntity = modelMapper.map(mediaFileDTO, MediaFileEntity.class);
        MediaFileEntity newMediaFile = mediaFileService.createMediaFile(mediaFileEntity);
        petMediaFileService.addPhotograph(newMediaFile.getId(), petId);
        return modelMapper.map(newMediaFile, MediaFileDTO.class);
    }

    // GET un archivo específico de una mascota
    @GetMapping(value = "/{petId}/mediaFiles/{mediaFileId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MediaFileDTO getMediaFile(@PathVariable Long petId, @PathVariable Long mediaFileId)
            throws EntityNotFoundException {
        MediaFileEntity mediaFileEntity = mediaFileService.getMediaFile(petId, mediaFileId);
        return modelMapper.map(mediaFileEntity, MediaFileDTO.class);
    }

    // PUT actualizar archivo
    @PutMapping(value = "/mediaFiles/{mediaFileId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MediaFileDTO updateMediaFile(@PathVariable Long mediaFileId,
            @RequestBody MediaFileDTO mediaFileDTO) throws EntityNotFoundException {
        MediaFileEntity mediaFileEntity = modelMapper.map(mediaFileDTO, MediaFileEntity.class);
        MediaFileEntity updatedMediaFile = mediaFileService.updateMediaFile(mediaFileId, mediaFileEntity);
        return modelMapper.map(updatedMediaFile, MediaFileDTO.class);
    }

    // DELETE eliminar archivo
    @DeleteMapping(value = "/mediaFiles/{mediaFileId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteMediaFile(@PathVariable Long mediaFileId)
            throws EntityNotFoundException, IllegalOperationException {
        mediaFileService.deleteMediaFile(mediaFileId);
    }
}