package be.podor.review.repository;

import be.podor.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"theaterSeat"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Review> findByMusical_MusicalIdOrderByCreatedAtDesc(Long musicalId, Pageable pageable);
}
