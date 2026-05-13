package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.VetVisitEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VetVisitRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VetVisitService {

    @Autowired
    private VetVisitRepository vetVisitRepository;

    @Transactional
    public VetVisitEntity createVetVisit(VetVisitEntity vetVisitEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creacion de visita veterinaria");

        //revisa que el archivo tenga todos los datos llenos
        if (vetVisitEntity.getVisitDate() != null && isStringValid(vetVisitEntity.getDescription())) {

            return vetVisitRepository.save(vetVisitEntity);

        } else {
            throw new IllegalOperationException("todos los campos tienen que estar llenos");
        }

    }

    @Transactional
    public VetVisitEntity updateVetVisit(Long id, VetVisitEntity vetVisitEntity) throws EntityNotFoundException {
        log.info("Inicia proceso de actualización de visita medica");

        Optional<VetVisitEntity> vetVisit = vetVisitRepository.findById(id);
        if (vetVisit.isEmpty()) {
            throw new EntityNotFoundException("visita medica no encontrada");
        }

        VetVisitEntity existingVetVisit = vetVisit.get();

        if (vetVisitEntity.getVisitDate() != null) {
            existingVetVisit.setVisitDate(vetVisitEntity.getVisitDate());
        }

        if (vetVisitEntity.getDescription() != null) {
            existingVetVisit.setDescription(vetVisitEntity.getDescription());
        }


        log.info("Termina proceso de actualización de visita medica");
        return vetVisitRepository.save(existingVetVisit);
    }

    @Transactional
    public void deleteVetVisit(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("inicia proceso de borrar visita medica");
        Optional<VetVisitEntity> vetVisitEntity = vetVisitRepository.findById(id);
        if (vetVisitEntity.isEmpty()) {
            throw new EntityNotFoundException("visita medica no encontrada");
        }

        vetVisitRepository.deleteById(id);
        log.info("Proceso de borrado terminado");
    }

    private boolean isStringValid(String texto) {
        return !(texto == null || texto.isEmpty());
    }

    //Se añadieron los getters de Vet Visist y la lista de Vet Visits.
    @Transactional
    public VetVisitEntity getVetVisit(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consulta de visita medica");
        Optional<VetVisitEntity> vetVisitEntity = vetVisitRepository.findById(id);
        if (vetVisitEntity.isEmpty()) {
            throw new EntityNotFoundException("Visita medica no encontrada");
        }
        log.info("Termina el proceso de consulta de visita medica");
        return vetVisitEntity.get();
    }

    @Transactional
    public java.util.List<VetVisitEntity> getVetVisits() {
        log.info("Inicia proceso de consulta de todas las visitas medicas");
        return vetVisitRepository.findAll();
    }
}
