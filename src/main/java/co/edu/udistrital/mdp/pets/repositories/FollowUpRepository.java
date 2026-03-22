package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.FollowUpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUpEntity, Long> {

    // Buscar todos los seguimientos de una mascota
    List<FollowUpEntity> findByPetId(Long petId);

    // Buscar todos los seguimientos asignados a un veterinario
    List<FollowUpEntity> findByVeterinaryId(Long veterinaryId);
}