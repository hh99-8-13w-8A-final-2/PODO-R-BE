package be.podor.review.repository;

import be.podor.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"theaterSeat"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Review> findByMusical_MusicalIdOrderByCreatedAtDesc(Long musicalId, Pageable pageable);

    // 라이브 리뷰
    @EntityGraph(attributePaths = {"musical", "theaterSeat"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Review> findTop10ByOrderByCreatedAtDesc();

    List<Review> findByCreatedByOrderByCreatedAtDesc(Long createdBy);
}
