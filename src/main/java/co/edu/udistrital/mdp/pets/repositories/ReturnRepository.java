package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnRepository extends JpaRepository<ReturnEntity, Long> {

    // Buscar la devolución asociada a una adopción específica
    Optional<ReturnEntity> findByAdoptionId(Long adoptionId);
}
