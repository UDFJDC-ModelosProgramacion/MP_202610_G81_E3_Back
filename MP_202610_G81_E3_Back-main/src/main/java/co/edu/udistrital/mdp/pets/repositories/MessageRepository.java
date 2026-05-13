package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import java.util.List;
import java.time.LocalDate;


@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Long>{
    //Búsqueda por fecha.
    List<MessageEntity> findByDate(LocalDate date);
    //Búsqueda por autor. 
    List<MessageEntity> findByAuthor (String author);
}
