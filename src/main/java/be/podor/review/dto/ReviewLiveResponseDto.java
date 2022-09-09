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
                .imgUrl(review.getReviewFiles().get(0).getFilePath())
                .floor(theaterSeat.getFloor().toString())
                .section(theaterSeat.getSection())
                .row(theaterSeat.getSeatRow())
                .seat(theaterSeat.getSeat())
                .reviewScore(Double.parseDouble(review.getScore()))
                .evaluation(EvaluationDto.of(review.getEvaluation()))
                .build();
    }
}
