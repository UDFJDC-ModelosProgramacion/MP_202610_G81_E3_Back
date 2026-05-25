package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    // AGREGA ESTA LÍNEA EXACTAMENTE AQUÍ:
    Optional<ClientEntity> findByClientEmail(String clientEmail);

}