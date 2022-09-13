package be.podor.musical.repository;

import be.podor.musical.model.Musical;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MusicalRepository extends JpaRepository<Musical, Long> {

    @EntityGraph(attributePaths = {"theater"})
    List<Musical> findTop10ByOrderByOpenDateDesc();

    @EntityGraph(attributePaths = {"theater"})
    Optional<Musical> findByMusicalId(Long musicalId);

    @Query(value = "SELECT m " +
            "FROM Musical m JOIN FETCH m.theater " +
            "INNER JOIN Review r ON r.musical.musicalId = m.musicalId " +
            "GROUP BY r.musical.musicalId " +
            "ORDER BY COUNT(r) DESC")
    List<Musical> findPopularMusical(Pageable pageable);
}
