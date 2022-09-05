package be.podor.review.dto;

import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.BriefTag;
import be.podor.review.model.reviewfile.ReviewFile;
import be.podor.review.model.tag.ReviewTag;
import be.podor.review.model.tag.Tag;
import be.podor.theater.model.TheaterSeat;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponseDto {

    private Long reviewId;
//    private Long memberId;
//    private String memberNickname;
    private String reviewContent;
    private List<String> imgurls;
    private Long theaterId;
    private String theaterName;
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
//    private String reviewHits;
//    private String reviewHearts;
//    private List<CommentDto> comments;

    public static ReviewDetailResponseDto of(Review review) {
        List<String> imgUrls = review.getReviewFiles().stream()
                .map(ReviewFile::getFilePath)
                .collect(Collectors.toList());

        List<String> reviewTags = review.getReviewTags().stream()
                .map(ReviewTag::getTag)
                .map(Tag::getTag)
                .collect(Collectors.toList());

        TheaterSeat theaterSeat = review.getTheaterSeat();

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

        return ReviewDetailResponseDto.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getContent())
                .imgurls(imgUrls)
                .theaterId(theaterSeat.getTheater().getTheaterId())
                .theaterName(theaterSeat.getTheater().getTheaterName())
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
                .build();
    }
}
