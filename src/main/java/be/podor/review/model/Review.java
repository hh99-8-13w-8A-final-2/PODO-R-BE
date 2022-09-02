package be.podor.review.model;

import be.podor.musical.model.Musical;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.reviewInfo.BriefTag;
import be.podor.share.Timestamped;
import be.podor.theater.model.TheaterSeat;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;

    @Embedded
    @Column(nullable = false)
    private BriefTag briefTag;

    // Todo enum
    @Column
    private String seatGrade;

    @Column
    private Boolean operaGlass;

    @Column
    private Boolean block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musical_id")
    private Musical musical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private TheaterSeat theaterSeat;

    public static Review of(TheaterSeat theaterSeat, Musical musical, ReviewRequestDto requestDto) {
        BriefTag briefTag = BriefTag.builder()
                .gap(requestDto.getGap())
                .light(requestDto.getLight())
                .sight(requestDto.getSight())
                .sound((requestDto.getSound()))
                .build();

        boolean operaGlass = requestDto.getOperaGrass() != null && requestDto.getOperaGrass().equals("on");
        boolean blockSight = requestDto.getBlock() != null && requestDto.getBlock().equals("on");

        return Review.builder()
                .content(requestDto.getReviewContent())
                .imgUrl(requestDto.getImgUrl())
                .briefTag(briefTag)
                .seatGrade(requestDto.getSeatGrade())
                .operaGlass(operaGlass)
                .block(blockSight)
                .musical(musical)
                .theaterSeat(theaterSeat)
                .build();
    }
}
