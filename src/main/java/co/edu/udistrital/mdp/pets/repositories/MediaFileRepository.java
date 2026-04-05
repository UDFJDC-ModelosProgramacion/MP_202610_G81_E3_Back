package co.edu.udistrital.mdp.pets.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;


@Repository
public interface MediaFileRepository extends JpaRepository<MediaFileEntity,Long>{
    public MediaFileEntity findByPetIdAndId(Long petId, Long id);
}
