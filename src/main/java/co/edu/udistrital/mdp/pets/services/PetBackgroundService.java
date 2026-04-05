package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.BackgroundEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.BackgroundRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetBackgroundService {

    @Autowired
    private BackgroundRepository backgroundRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public PetEntity addBackground(Long backgroundId, Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de asociacion de antecedente con mascota");
        Optional<BackgroundEntity>backgroundEntity=backgroundRepository.findById(backgroundId);
        if(backgroundEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra antecedente");
        }
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        petEntity.get().getBackgrounds().add(backgroundEntity.get()); // se agrega el antecedente a la mascota
        backgroundEntity.get().setPet(petEntity.get());
        log.info("Termina proceso de asociacion de antecedente y mascota");
        return petEntity.get();
    }

    
    @Transactional
    public List<BackgroundEntity> getBackgrounds(Long petId)throws EntityNotFoundException{
        log.info("Iniciando proceso de consulta de antecedentes");
        Optional<PetEntity>petEntity=petRepository.findById(petId);
        if(petEntity.isEmpty()){
            throw new EntityNotFoundException("No se encuentra mascota");
        }
        log.info("Finaliza proceso de consulta");
        return petEntity.get().getBackgrounds();
    }
    
}
