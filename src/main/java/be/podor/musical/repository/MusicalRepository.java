package be.podor.musical.repository;

import be.podor.musical.model.Musical;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MusicalRepository extends JpaRepository<Musical, Long> {

    @EntityGraph(attributePaths = {"theater"})
    List<Musical> findTop10ByOrderByOpenDateDesc();

    @EntityGraph(attributePaths = {"theater"})
    Optional<Musical> findByMusicalId(Long musicalId);
}
