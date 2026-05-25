package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VeterinaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeterinaryRepository extends JpaRepository<VeterinaryEntity, Long> {

    // Buscar todos los veterinarios de un refugio
    List<VeterinaryEntity> findByShelterId(Long shelterId);

    // Buscar por especialidad
    List<VeterinaryEntity> findBySpecialty(String specialty);
}