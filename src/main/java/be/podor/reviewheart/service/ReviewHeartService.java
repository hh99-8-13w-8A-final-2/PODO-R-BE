package be.podor.reviewheart.service;

import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.reviewheart.model.ReviewHeart;
import be.podor.reviewheart.repository.ReviewHeartRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewHeartService {

    private final ReviewHeartRepository reviewHeartRepository;

    private final ReviewRepository reviewRepository;

    public void heartReview(Long reviewId, UserDetailsImpl userDetails) {
        Review review = reviewRepository.getReferenceById(reviewId);

        if (reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId())) {
            throw new IllegalArgumentException("같은 리뷰에 대해 좋아요를 두 번 지정할 수 없습니다.");
        }

        reviewHeartRepository.save(ReviewHeart.of(review));
    }
}
