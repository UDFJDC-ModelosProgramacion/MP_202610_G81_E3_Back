package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ReturnPetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReturnPetRepository;

@Service
public class ReturnPetService {

    @Autowired
    private ReturnPetRepository returnRepository;

    private static final String MSG_RETURN_NOT_FOUND = "La devolución con id %d no fue encontrada.";
    private static final String MSG_REASON_INVALID = "La razón de devolución no puede ser nula o vacía.";
    private static final String MSG_DATE_INVALID = "La fecha de devolución no puede ser en el futuro.";

    /**
     * Crea una nueva devolución.
     * Reglas de negocio:
     * - La razón no puede ser nula ni vacía.
     * - La fecha de devolución no puede ser en el futuro.
     */
    @Transactional
    public ReturnPetEntity createReturn(ReturnPetEntity returnEntity) throws IllegalOperationException {
        if (returnEntity.getReason() == null || returnEntity.getReason().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_REASON_INVALID);
        }
        if (returnEntity.getReturnDate() != null && returnEntity.getReturnDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException(MSG_DATE_INVALID);
        }
        return returnRepository.save(returnEntity);
    }

    /**
     * Obtiene todas las devoluciones.
     */
    @Transactional(readOnly = true)
    public List<ReturnPetEntity> getReturns() {
        return returnRepository.findAll();
    }

    /**
     * Obtiene una devolución por ID.
     */
    @Transactional(readOnly = true)
    public ReturnPetEntity getReturn(Long id) throws EntityNotFoundException {
        Optional<ReturnPetEntity> returnEntity = returnRepository.findById(id);
        if (returnEntity.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_RETURN_NOT_FOUND, id));
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
    public ReturnPetEntity updateReturn(Long id, ReturnPetEntity returnEntity)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<ReturnPetEntity> existing = returnRepository.findById(id);
        if (existing.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_RETURN_NOT_FOUND, id));
        }
        if (returnEntity.getReason() == null || returnEntity.getReason().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_REASON_INVALID);
        }
        if (returnEntity.getReturnDate() != null && returnEntity.getReturnDate().isAfter(LocalDate.now())) {
            throw new IllegalOperationException(MSG_DATE_INVALID);
        }
        returnEntity.setId(id);
        return returnRepository.save(returnEntity);
    }

    /**
     * Elimina una devolución por ID.
     */
    @Transactional
    public void deleteReturn(Long id) throws EntityNotFoundException {
        Optional<ReturnPetEntity> returnEntity = returnRepository.findById(id);
        if (returnEntity.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_RETURN_NOT_FOUND, id));
        }
        returnRepository.deleteById(id);
    }
}