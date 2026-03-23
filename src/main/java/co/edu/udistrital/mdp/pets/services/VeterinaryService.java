package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VeterinaryRepository;

@Service
public class VeterinaryService {

    @Autowired
    private VeterinaryRepository veterinaryRepository;

    private static final String MSG_NAME_INVALID = "El nombre del veterinario no puede ser nulo o vacío.";
    private static final String MSG_EMAIL_INVALID = "El email del veterinario no puede ser nulo o vacío.";
    private static final String MSG_SPECIALTY_INVALID = "La especialidad del veterinario no puede ser nula o vacía.";
    private static final String MSG_VETERINARY_NOT_FOUND = "El veterinario con id %d no fue encontrado.";

    @Transactional
    public VeterinaryEntity createVeterinary(VeterinaryEntity veterinary) throws IllegalOperationException {
        if (veterinary.getName() == null || veterinary.getName().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_NAME_INVALID);
        }
        if (veterinary.getEmail() == null || veterinary.getEmail().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_EMAIL_INVALID);
        }
        if (veterinary.getSpecialty() == null || veterinary.getSpecialty().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_SPECIALTY_INVALID);
        }
        return veterinaryRepository.save(veterinary);
    }

    @Transactional(readOnly = true)
    public List<VeterinaryEntity> getVeterinaries() {
        return veterinaryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public VeterinaryEntity getVeterinary(Long id) throws EntityNotFoundException {
        Optional<VeterinaryEntity> veterinary = veterinaryRepository.findById(id);
        if (veterinary.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_VETERINARY_NOT_FOUND, id));
        }
        return veterinary.get();
    }

    @Transactional
    public VeterinaryEntity updateVeterinary(Long id, VeterinaryEntity veterinary)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<VeterinaryEntity> existing = veterinaryRepository.findById(id);
        if (existing.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_VETERINARY_NOT_FOUND, id));
        }
        if (veterinary.getName() == null || veterinary.getName().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_NAME_INVALID);
        }
        if (veterinary.getEmail() == null || veterinary.getEmail().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_EMAIL_INVALID);
        }
        if (veterinary.getSpecialty() == null || veterinary.getSpecialty().trim().isEmpty()) {
            throw new IllegalOperationException(MSG_SPECIALTY_INVALID);
        }
        veterinary.setId(id);
        return veterinaryRepository.save(veterinary);
    }

    @Transactional
    public void deleteVeterinary(Long id) throws EntityNotFoundException {
        Optional<VeterinaryEntity> veterinary = veterinaryRepository.findById(id);
        if (veterinary.isEmpty()) {
            throw new EntityNotFoundException(String.format(MSG_VETERINARY_NOT_FOUND, id));
        }
        veterinaryRepository.deleteById(id);
    }
}