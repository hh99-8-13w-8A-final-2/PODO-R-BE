package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.BriefTag;
import be.podor.review.model.reviewInfo.ScoreEnum;
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

    private String gap;

    private String sight;

    private String sound;

    private String light;

    public static ReviewLiveResponseDto of(Review review) {
        Double score = 0.0;

        BriefTag briefTag = review.getBriefTag();

        score += briefTag.getGap().getScore();
        score += briefTag.getLight().getScore();
        score += briefTag.getSight().getScore();
        score += briefTag.getSound().getScore();

        // 0 ~ 10
        score = (score - 4) / 8 * 10;
        // 0.5 단위 절삭
        score = Math.ceil(score * 2) / 2;

        return ReviewLiveResponseDto.builder()
                .reviewId(review.getReviewId())
                .musicalName(review.getMusical().getMusicalName())
                .reviewContent(review.getContent())
                .gap(briefTag.getGap().getText())
                .sight(briefTag.getSight().getText())
                .sound(briefTag.getSound().getText())
                .light(briefTag.getLight().getText())
                .reviewScore(String.format("%.1f", score))
                .build();
    }
}
