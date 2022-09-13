package be.podor.reviewheart.repository;

import be.podor.review.model.Review;
import be.podor.reviewheart.model.ReviewHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHeartRepository extends JpaRepository<ReviewHeart, Long> {

    boolean existsByReviewAndCreatedBy(Review review, Long memberId);

    void deleteByReviewAndCreatedBy(Review review, Long memberId);

}
