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

    public static ReviewLiveResponseDto of(Review review) {
        Double score = 0.0;

        score += review.getBriefTag().getGap().getScore();
        score += review.getBriefTag().getLight().getScore();
        score += review.getBriefTag().getSight().getScore();
        score += review.getBriefTag().getSound().getScore();

        // 0 ~ 10
        score = (score - 4) / 8 * 10;
        // 0.5 단위 절삭
        score = Math.ceil(score * 2) / 2;

        return ReviewLiveResponseDto.builder()
                .reviewId(review.getReviewId())
                .musicalName(review.getMusical().getMusicalName())
                .reviewContent(review.getContent())
                .reviewScore(String.format("%.1f", score))
                .build();
    }
}
