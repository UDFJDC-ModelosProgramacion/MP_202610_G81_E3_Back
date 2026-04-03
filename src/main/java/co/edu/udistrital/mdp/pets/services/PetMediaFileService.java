package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.MediaFileRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetMediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private PetRepository petRepository;


    @Transactional
    public PetEntity addPhotograph(Long mediaFileId, Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de asociacion de evento medico con mascota");
        Optional<MediaFileEntity>mediaFileEntity=mediaFileRepository.findById(mediaFileId);
        if(mediaFileEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra archivo");
        }
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        petEntity.get().getPhotographes().add(mediaFileEntity.get()); // se agrega la fotografia a la mascota
        log.info("Termina proceso de asociacion de foto y mascota");
        return petEntity.get();
    }

    @Transactional
    public List<MediaFileEntity> getPhotographes(Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de consulta de fotos");
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        log.info("Finaliza proceso de consulta");
        return petEntity.get().getPhotographes();
    }
    
}
