package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ReturnPetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnPetRepository extends JpaRepository<ReturnPetEntity, Long> {

    // Buscar la devolución asociada a una adopción específica
    Optional<ReturnPetEntity> findByAdoptionId(Long adoptionId);
}
