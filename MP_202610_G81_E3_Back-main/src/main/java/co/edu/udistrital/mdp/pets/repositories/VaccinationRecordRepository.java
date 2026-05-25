package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecordEntity,Long>{
    public List<VaccinationRecordEntity> findByVaccineName(String vacineName);
    public VaccinationRecordEntity findByPetIdAndId(Long petId, Long id);

}
