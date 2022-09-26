package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.theater.model.TheaterSeat;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewLiveResponseDto {

    private Long reviewId;

    private Long musicalId;

    private String imgUrl;

    private String floor;

    private String section;

    private String row;

    private Integer seat;

    private Double reviewScore;

    private EvaluationDto evaluation;

    public static ReviewLiveResponseDto of(Review review) {
        TheaterSeat theaterSeat = review.getTheaterSeat();

        return ReviewLiveResponseDto.builder()
                .reviewId(review.getReviewId())
                .musicalId(review.getMusical().getMusicalId())
                .imgUrl(review.getMusical().getMusicalPoster())
                .floor(theaterSeat.getFloor().getFloor())
                .section(theaterSeat.getSection())
                .row(theaterSeat.getSeatRow())
                .seat(theaterSeat.getSeat())
                .reviewScore(review.getScore())
                .evaluation(EvaluationDto.of(review.getEvaluation()))
                .build();
    }
}
