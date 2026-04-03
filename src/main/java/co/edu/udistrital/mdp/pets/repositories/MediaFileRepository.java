package co.edu.udistrital.mdp.pets.repositories;
import co.edu.udistrital.mdp.pets.entities.MediaFileEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MediaFileRepository extends JpaRepository<MediaFileEntity,Long>{

}
