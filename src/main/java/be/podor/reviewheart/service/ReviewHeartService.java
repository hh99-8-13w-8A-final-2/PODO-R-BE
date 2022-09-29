package be.podor.reviewheart.service;

import be.podor.exception.podor.PodoalException;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.reviewheart.model.ReviewHeart;
import be.podor.reviewheart.repository.ReviewHeartRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewHeartService {

    private final ReviewHeartRepository reviewHeartRepository;

    private final ReviewRepository reviewRepository;

    public void heartReview(Long reviewId, UserDetailsImpl userDetails) {
        Review review = reviewRepository.getReferenceById(reviewId);

        if (reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId())) {
            throw PodoalException.DOUBLE_HEART_EXCEPTION;
        }

        reviewHeartRepository.save(ReviewHeart.of(review));
    }

    @Transactional
    public void unHeartReview(Long reviewId, UserDetailsImpl userDetails) {
        Review review = reviewRepository.getReferenceById(reviewId);

        if (!reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId())) {
            throw PodoalException.NO_HEART_EXCEPTION;
        }

        reviewHeartRepository.deleteByReviewAndCreatedBy(review, userDetails.getMemberId());
    }
}
