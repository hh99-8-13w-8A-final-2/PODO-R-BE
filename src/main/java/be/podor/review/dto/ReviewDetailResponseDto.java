package be.podor.review.dto;

import be.podor.member.dto.MemberDto;
import be.podor.musical.dto.MusicalResponseDto;
import be.podor.musical.model.Musical;
import be.podor.review.model.Review;
import be.podor.review.model.reviewfile.ReviewFile;
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
public class ReviewDetailResponseDto {
    private MusicalResponseDto musical;
    private MemberDto member;
    private Long reviewId;
    private String reviewContent;
    private List<String> imgurls;
    private String grade;
    private String floor;
    private String section;
    private String row;
    private Integer seat;
    private EvaluationDto evaluation;
    private String reviewScore;
    private Boolean operaGlass;
    private Boolean block;
    private List<String> tags;
    private LocalDateTime createdAt;
//    private String reviewHits;
//    private String reviewHearts;
    private Integer commentCount;

    public static ReviewDetailResponseDto of(Review review, MemberDto member) {
        List<String> imgUrls = review.getReviewFiles().stream()
                .map(ReviewFile::getFilePath)
                .collect(Collectors.toList());

        List<String> reviewTags = review.getReviewTags().stream()
                .map(ReviewTag::getTag)
                .map(Tag::getTag)
                .collect(Collectors.toList());

        TheaterSeat theaterSeat = review.getTheaterSeat();

        return ReviewDetailResponseDto.builder()
                .musical(MusicalResponseDto.of(review.getMusical()))
                .member(member)
                .reviewId(review.getReviewId())
                .reviewContent(review.getContent())
                .imgurls(imgUrls)
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
                .build();
    }
}
