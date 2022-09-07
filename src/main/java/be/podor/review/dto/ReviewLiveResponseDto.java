package be.podor.review.dto;

import be.podor.review.model.Review;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewLiveResponseDto {

    private Long reviewId;

    private String musicalName;

    private String reviewContent;

    private String reviewScore;

    private EvaluationDto evaluation;

    public static ReviewLiveResponseDto of(Review review) {
        return ReviewLiveResponseDto.builder()
                .reviewId(review.getReviewId())
                .musicalName(review.getMusical().getMusicalName())
                .reviewContent(review.getContent())
                .reviewScore(review.getScore())
                .evaluation(EvaluationDto.of(review.getEvaluation()))
                .build();
    }
}
