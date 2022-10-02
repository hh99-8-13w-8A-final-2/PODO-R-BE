package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.review.model.tag.ReviewTag;
import be.podor.tag.model.Tag;
import be.podor.theater.model.TheaterSeat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewListResponseDto {
    private Long musicalId;
    private Long reviewId;
    private String imgUrl;
    private Long theaterId;
    private String grade;
    private String floor;
    private String section;
    private String row;
    private Integer seat;
    private EvaluationDto evaluation;
    private Double reviewScore;
    private Boolean operaGlass;
    private Boolean block;
    private List<String> tags;
    private LocalDateTime createdAt;
    private Integer commentCount;
    private Integer heartCount;
    private Boolean heartChecked;

    public static ReviewListResponseDto of(Review review, Boolean heartChecked) {
        TheaterSeat theaterSeat = review.getTheaterSeat();

        List<String> reviewTags = review.getReviewTags().stream()
                .map(ReviewTag::getTag)
                .map(Tag::getTag)
                .collect(Collectors.toList());

        return ReviewListResponseDto.builder()
                .musicalId(review.getMusical().getMusicalId())
                .reviewId(review.getReviewId())
                .imgUrl(review.getReviewFiles().get(0).getFilePath())
                .theaterId(theaterSeat.getTheater().getTheaterId())
                .grade(review.getGrade().toString())
                .floor(theaterSeat.getFloor().getFloor())
                .section(theaterSeat.getSection())
                .row(theaterSeat.getSeatRow())
                .seat(theaterSeat.getSeat())
                .evaluation(EvaluationDto.of(review.getEvaluation()))
                .reviewScore(review.getScore())
                .operaGlass(review.getOperaGlass())
                .block(review.getBlock())
                .tags(reviewTags)
                .createdAt(review.getCreatedAt())
                .commentCount(review.getComments().size())
                .heartCount(review.getReviewHearts().size())
                .heartChecked(heartChecked)
                .build();
    }
}
