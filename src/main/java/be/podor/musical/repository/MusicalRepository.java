package be.podor.musical.repository;

import be.podor.musical.model.Musical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicalRepository extends JpaRepository<Musical, Long> {

}
