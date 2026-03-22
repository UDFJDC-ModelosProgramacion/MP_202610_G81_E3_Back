package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ReturnEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReturnRepository;

@Service
public class ReturnService {

    @Autowired
    private ReturnRepository returnRepository;

    /**
     * Crea una nueva devolución.
     * Reglas de negocio:
     * - La razón no puede ser nula ni vacía.
     * - La fecha de devolución no puede ser en el futuro.
     */
    @Transactional
    public ReturnEntity createReturn(ReturnEntity returnEntity) throws IllegalOperationException {
        if (returnEntity.getReason() == null || returnEntity.getReason().trim().isEmpty()) {
            throw new IllegalOperationException("La razón de devolución no puede ser nula o vacía.");
        }
        if (returnEntity.getReturnDate() != null && returnEntity.getReturnDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException("La fecha de devolución no puede ser en el futuro.");
        }
        return returnRepository.save(returnEntity);
    }

    /**
     * Obtiene todas las devoluciones.
     */
    @Transactional(readOnly = true)
    public List<ReturnEntity> getReturns() {
        return returnRepository.findAll();
    }

    /**
     * Obtiene una devolución por ID.
     */
    @Transactional(readOnly = true)
    public ReturnEntity getReturn(Long id) throws EntityNotFoundException {
        Optional<ReturnEntity> returnEntity = returnRepository.findById(id);
        if (returnEntity.isEmpty()) {
            throw new EntityNotFoundException("La devolución con id " + id + " no fue encontrada.");
        }
        return returnEntity.get();
    }

    /**
     * Actualiza una devolución existente.
     * Reglas de negocio:
     * - La razón no puede ser nula ni vacía.
     * - La fecha de devolución no puede ser en el futuro.
     */
    @Transactional
    public ReturnEntity updateReturn(Long id, ReturnEntity returnEntity)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<ReturnEntity> existing = returnRepository.findById(id);
        if (existing.isEmpty()) {
            throw new EntityNotFoundException("La devolución con id " + id + " no fue encontrada.");
        }
        if (returnEntity.getReason() == null || returnEntity.getReason().trim().isEmpty()) {
            throw new IllegalOperationException("La razón de devolución no puede ser nula o vacía.");
        }
        if (returnEntity.getReturnDate() != null && returnEntity.getReturnDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException("La fecha de devolución no puede ser en el futuro.");
        }
        returnEntity.setId(id);
        return returnRepository.save(returnEntity);
    }

    /**
     * Elimina una devolución por ID.
     */
    @Transactional
    public void deleteReturn(Long id) throws EntityNotFoundException {
        Optional<ReturnEntity> returnEntity = returnRepository.findById(id);
        if (returnEntity.isEmpty()) {
            throw new EntityNotFoundException("La devolución con id " + id + " no fue encontrada.");
        }
        returnRepository.deleteById(id);
    }
}