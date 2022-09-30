package be.podor.review.validator;

import be.podor.exception.podor.PodoalException;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;

public class ReviewValidator {

    public static Review validate(ReviewRepository reviewRepository, Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> PodoalException.NO_REVIEW_EXCEPTION);
    }
}
