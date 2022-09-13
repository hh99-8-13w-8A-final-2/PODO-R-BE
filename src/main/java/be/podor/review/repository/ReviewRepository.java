package be.podor.review.repository;

import be.podor.musical.model.Musical;
import be.podor.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"theaterSeat"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Review> findByMusical_MusicalIdOrderByCreatedAtDesc(Long musicalId, Pageable pageable);

    // 라이브 리뷰
    @EntityGraph(attributePaths = {"musical", "theaterSeat"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Review> findTop10ByOrderByCreatedAtDesc();

    Page<Review> findByCreatedByOrderByCreatedAtDesc(Long createdBy, Pageable pageable);

    void deleteByReviewIdAndCreatedBy(Long reviewId, Long memberId);

    //리뷰에 들어있는 생성자를 통해 뮤지컬 조회
    @Query(value = "SELECT ri.musical FROM Review ri WHERE ri.createdBy = :createdBy GROUP BY ri.musical.musicalId")
    Page<Musical> findByReviewIdGroupByMusical(Long createdBy, Pageable pageable);
    Page<Review> findByMusical_MusicalIdAndCreatedBy(Long createdBy,Long musicalId,Pageable pageable);
}
