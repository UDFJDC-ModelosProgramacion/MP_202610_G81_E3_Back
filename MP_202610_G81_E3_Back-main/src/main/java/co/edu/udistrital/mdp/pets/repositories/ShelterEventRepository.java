package co.edu.udistrital.mdp.pets.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import org.springframework.stereotype.Repository;

//Repositorio para ShelterEventEntity, con métodos de búsqueda personalizados.

@Repository
public interface ShelterEventRepository extends JpaRepository <ShelterEventEntity, Long>{
    //Busqueda por nombre.
    List<ShelterEventEntity> findByNameContainingIgnoreCase(String name);
    //Busqueda por rango de fechas.
    List<ShelterEventEntity> findByDateBetween(LocalDate startDate,LocalDate endDate);
    //Busqueda por fechas.
    List<ShelterEventEntity> findByDate(LocalDate startDate);
}
