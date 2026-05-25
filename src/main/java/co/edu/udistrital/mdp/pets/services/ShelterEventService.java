package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterEventRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.List;

@Slf4j
@Service
public class ShelterEventService {
    @Autowired
    //Repositorio de eventos de refugios, para persistencia.
    private ShelterEventRepository shelterEventRepository;

    //Repositorio de refugios, para persistencia.
    @Autowired
    private ShelterRepository shelterRepository;

    //Metodo para evaluar la validez del evento.
    private void validateShelterEvent(ShelterEventEntity shelterEventEntity)
    throws IllegalOperationException {
        //Valida si la fecha es nula.
        if(shelterEventEntity.getDate() == null)
            throw new IllegalOperationException("Date isn't valid.");

        //Valida si el nombre es nulo.
        if(shelterEventEntity.getName() == null)
            throw new IllegalOperationException("Name isn't valid.");

        //Valida si la descripcion es nula.
        if(shelterEventEntity.getDescription() == null)
            throw new IllegalOperationException("Description isn't valid.");

        //Validar que el evento tenga un refugio asociado.
        if(shelterEventEntity.getShelter() == null)
            throw new IllegalOperationException("Shelter isn't valid.");
    }

    //Metodo para crear un evento
    @Transactional
    public ShelterEventEntity createShelterEvent(ShelterEventEntity shelterEventEntity)
        throws IllegalOperationException, EntityNotFoundException {
    log.info("Start shelter event creation...");

    // Valida datos básicos.
    validateShelterEvent(shelterEventEntity);
    Long shelterId = shelterEventEntity.getShelter().getId();

    if (shelterId == null) {
        throw new IllegalOperationException("Shelter ID is required");
    }
    ShelterEntity shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
    shelterEventEntity.setShelter(shelter);

    // Validar fecha repetida para el mismo refugio.
    List<ShelterEventEntity> events = shelterEventRepository.findByDate(shelterEventEntity.getDate());
    for (ShelterEventEntity e : events) {
        if (e.getShelter().getId().equals(shelterId)) {
            throw new IllegalOperationException("There is already an event with that date in this shelter.");
        }
    }
    return shelterEventRepository.save(shelterEventEntity);
    }

    //Metodo para editar un evento.
    @Transactional
    public ShelterEventEntity updateShelterEventEntity(Long shelterEventId, ShelterEventEntity event)
        throws IllegalOperationException, EntityNotFoundException { 
            log.info("Starts update shelter event with id: {}", shelterEventId);
    // Busca evento existente
    ShelterEventEntity shelterEventToUpdate = shelterEventRepository.findById(shelterEventId)
        .orElseThrow(() -> new EntityNotFoundException("Shelter event not found."));
    validateShelterEvent(event);

    Long shelterId = event.getShelter().getId();

    if (shelterId == null) {
        throw new IllegalOperationException("Shelter ID is required");
    }

    ShelterEntity shelter = shelterRepository.findById(shelterId)
            .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
    List<ShelterEventEntity> dateCheck = shelterEventRepository.findByDate(event.getDate());

    for (ShelterEventEntity e : dateCheck) {
        if (!e.getId().equals(shelterEventId)
                && e.getShelter().getId().equals(shelterId)) {

            throw new IllegalOperationException(
                    "There is already an event with that date in this shelter.");
        }
    }

    shelterEventToUpdate.setName(event.getName());
    shelterEventToUpdate.setDescription(event.getDescription());
    shelterEventToUpdate.setDate(event.getDate());
    shelterEventToUpdate.setShelter(shelter);
    log.info("End update shelter event with the id: {}", shelterEventId);
    return shelterEventRepository.save(shelterEventToUpdate);
    }

    //Metodo para borrar un evento existente.
    @Transactional
    public void deleteShelterEvent(Long shelterEventId) 
    throws EntityNotFoundException, IllegalOperationException{

        log.info("Start shelter event delete with the id: {}", shelterEventId);

        //Busca el evento.
        Optional<ShelterEventEntity> shelterEventEntity = shelterEventRepository.findById(shelterEventId);

        //Verifica que el evento exista.
        if(shelterEventEntity.isEmpty())
            throw new EntityNotFoundException("Shelter event not found.");

        //Elimina el evento.
        shelterEventRepository.deleteById(shelterEventId);

        log.info("End delete shelter event process with id: {}", shelterEventId);
    }

    public List<ShelterEventEntity> getEvents() {
        log.info("Fetching all shelter events");
        return shelterEventRepository.findAll();
    }

    public ShelterEventEntity getEvent(Long id) throws EntityNotFoundException {
        log.info("Fetching shelter event with id: {}", id);

        return shelterEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter event not found"));
    }
}