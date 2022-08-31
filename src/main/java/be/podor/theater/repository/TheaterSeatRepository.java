package be.podor.theater.repository;

import be.podor.theater.model.TheaterSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterSeatRepository extends JpaRepository<TheaterSeat, Long> {

}
