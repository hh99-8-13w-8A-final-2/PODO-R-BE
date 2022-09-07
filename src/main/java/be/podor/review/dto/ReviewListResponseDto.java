package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.review.model.tag.ReviewTag;
import be.podor.review.model.tag.Tag;
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
    private Long reviewId;
    //    private String memberNickname;
    private String imgUrl;
    //    private String theaterName;
    private String grade;
    private String floor;
    private String section;
    private String row;
    private Integer seat;
    private EvaluationDto evaluation;
    private Double reviewScore;
    private Boolean operaGrass;
    private Boolean block;
    private List<String> tags;
    //    private String reviewReads;
//    private String reviewHearts;
    private LocalDateTime createdAt;

    // 불 필요한 정보들 뷰 완성되면 덜어내기
    public static ReviewListResponseDto of(Review review) {
        TheaterSeat theaterSeat = review.getTheaterSeat();

        List<String> reviewTags = review.getReviewTags().stream()
                .map(ReviewTag::getTag)
                .map(Tag::getTag)
                .collect(Collectors.toList());

        return ReviewListResponseDto.builder()
                .reviewId(review.getReviewId())
//                .memberNickname()
                .imgUrl(review.getReviewFiles().get(0).getFilePath())
//                .theaterName()
                .grade(review.getGrade().toString())
                .floor(theaterSeat.getFloor().getFloor())
                .section(theaterSeat.getSection())
                .row(theaterSeat.getSeatRow())
                .seat(theaterSeat.getSeat())
                .evaluation(EvaluationDto.of(review.getEvaluation()))
                .reviewScore(Double.parseDouble(review.getScore()))
                .operaGrass(review.getOperaGlass())
                .block(review.getBlock())
                .tags(reviewTags)
//                .reviewReads()
//                .reviewHearts()
                .createdAt(review.getCreatedAt())
                .build();
    }
}
