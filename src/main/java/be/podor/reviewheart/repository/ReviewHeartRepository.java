package be.podor.reviewheart.repository;

import be.podor.review.model.Review;
import be.podor.reviewheart.model.ReviewHeart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewHeartRepository extends JpaRepository<ReviewHeart, Long> {

    boolean existsByReviewAndCreatedBy(Review review, Long memberId);

    void deleteByReviewAndCreatedBy(Review review, Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ReviewHeart h SET h.createdBy = :leaveMemberId WHERE h.createdBy = :createdBy")
    void updateCreatedBy(Long leaveMemberId, Long createdBy);

}
