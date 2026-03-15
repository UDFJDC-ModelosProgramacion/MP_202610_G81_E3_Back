package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterEventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.List;

@Slf4j
@Service
public class ShelterEventService {
    @Autowired
    //Repositorio de refugios, para persistencia.
    private ShelterEventRepository shelterEventRepository;

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
    public ShelterEventEntity createShelterEvent (ShelterEventEntity shelterEventEntity)
    throws IllegalOperationException, EntityNotFoundException{
        log.info("Start shelter event creation...");
        //Uso del metodo privado.
        validateShelterEvent(shelterEventEntity);

        List<ShelterEventEntity> events = shelterEventRepository.findByDate(shelterEventEntity.getDate());

        //Verificar si ya existe evento con esa fecha.
        if(!events.isEmpty())
            throw new IllegalOperationException("There is already an event with that date.");

        //Si todo es correcto guarda el evento
        return shelterEventRepository.save(shelterEventEntity);
    }

    //Metodo para editar un evento.
    @Transactional
    public ShelterEventEntity updateShelterEventEntity (long shelterEventId, ShelterEventEntity event)
    throws IllegalOperationException, EntityNotFoundException{
        log.info("Starts update shelter event with id: {}", shelterEventId);

        //Busca el evento del refugio.
        Optional<ShelterEventEntity> shelterEventEntity = shelterEventRepository.findById(shelterEventId);

        //Verifica su existencia.
        if(shelterEventEntity.isEmpty())
            throw new EntityNotFoundException("Shelter event not found.");

        //Obtiene el refugio a actualizar.
        ShelterEventEntity shelterEventToUpdate = shelterEventEntity.get();

        //Uso del método.
        validateShelterEvent(event);

        //Verificar fecha.
        List<ShelterEventEntity> dateCheck = shelterEventRepository.findByDate(event.getDate());

        for (ShelterEventEntity e : dateCheck) {
            if(!e.getId().equals(shelterEventId)) {
                throw new IllegalOperationException("There is already an event with that date.");
            }
        }


        //Actualizar si cumple con todas las condiciones.
        shelterEventToUpdate.setShelter(event.getShelter());
        shelterEventToUpdate.setName(event.getName());
        shelterEventToUpdate.setDate(event.getDate());
        shelterEventToUpdate.setDescription(event.getDescription());

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
}