package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.PetEntity;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    @Query("SELECT p FROM PetEntity p WHERE "
            + "(:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND "
            + "(:species IS NULL OR :species = '' OR "
            + "  (:species = 'Otros' AND LOWER(p.species) NOT IN ('perro', 'gato')) OR "
            + "  (:species != 'Otros' AND LOWER(p.species) = LOWER(:species))) AND "
            + "(:minAge IS NULL OR p.age >= :minAge) AND "
            + "(:maxAge IS NULL OR p.age <= :maxAge) AND "
            + "(:size IS NULL OR :size = '' OR LOWER(p.size) = LOWER(:size)) AND "
            + "(:requirements IS NULL OR :requirements = '' OR LOWER(p.specificRequirements) LIKE LOWER(CONCAT('%', :requirements, '%'))) AND "
            + "(:requiredSpace IS NULL OR :requiredSpace = '' OR LOWER(p.requiredSpace) = LOWER(:requiredSpace))")
    List<PetEntity> searchByKeywordAndFilters(
            @Param("keyword") String keyword,
            @Param("species") String species,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("size") String size,
            @Param("requirements") String requirements,
            @Param("requiredSpace") String requiredSpace
    );
}
