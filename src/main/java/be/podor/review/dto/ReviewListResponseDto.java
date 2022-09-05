package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.BriefTag;
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
    private String floor;
    private String section;
    private String row;
    private Integer seat;
    private String gap;
    private String sight;
    private String sound;
    private String light;
    private String reviewScore;
    private Boolean operaGrass;
    private Boolean block;
    private List<String> tags;
    //    private String reviewReads;
//    private String reviewHearts;
    private LocalDateTime createdAt;

    // 불 필요한 정보들 뷰 완성되면 덜어내기
    public static ReviewListResponseDto of(Review review) {
        TheaterSeat theaterSeat = review.getTheaterSeat();
        BriefTag briefTag = review.getBriefTag();
        List<String> reviewTags = review.getReviewTags().stream()
                .map(ReviewTag::getTag)
                .map(Tag::getTag)
                .collect(Collectors.toList());

        Double score = 0.0;

        score += briefTag.getGap().getScore();
        score += briefTag.getLight().getScore();
        score += briefTag.getSight().getScore();
        score += briefTag.getSound().getScore();

        // 0 ~ 10
        score = (score - 4) / 8 * 10;
        // 0.5 단위 절삭
        score = Math.ceil(score * 2) / 2;

        return ReviewListResponseDto.builder()
                .reviewId(review.getReviewId())
//                .memberNickname()
                .imgUrl(review.getReviewFiles().get(0).getFilePath())
//                .theaterName()
                .floor(theaterSeat.getFloor().getFloor())
                .section(theaterSeat.getSection())
                .row(theaterSeat.getSeatRow())
                .seat(theaterSeat.getSeat())
                .gap(briefTag.getGap().getText())
                .sight(briefTag.getSight().getText())
                .sound(briefTag.getSound().getText())
                .light(briefTag.getLight().getText())
                .reviewScore(String.format("%.1f", score))
                .operaGrass(review.getOperaGlass())
                .block(review.getBlock())
                .tags(reviewTags)
//                .reviewReads()
//                .reviewHearts()
                .createdAt(review.getCreatedAt())
                .build();
    }
}
