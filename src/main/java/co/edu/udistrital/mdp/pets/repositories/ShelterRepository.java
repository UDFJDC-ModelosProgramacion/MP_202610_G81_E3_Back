package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository <ShelterEntity, Long>{
    
    //Búsqueda por nombre ignore case.
    List<ShelterEntity> findByNameContainingIgnoreCase(String name);
    //Búsqueda por nombre.
    List<ShelterEntity> findByName(String name);
    //Búsqueda por ciudad.
    List<ShelterEntity> findByCity(String city);
    //Búsqueda por ID.
    Optional<ShelterEntity> findById(long id);
    //Búsqueda por email.
    Optional<ShelterEntity> findByEmail(String email);

}