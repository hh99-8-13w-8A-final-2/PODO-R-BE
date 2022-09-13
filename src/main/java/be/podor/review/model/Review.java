package be.podor.review.model;

import be.podor.comment.model.Comment;
import be.podor.musical.model.Musical;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.reviewInfo.Evaluation;
import be.podor.review.model.reviewfile.ReviewFile;
import be.podor.review.model.tag.ReviewTag;
import be.podor.reviewheart.model.ReviewHeart;
import be.podor.share.BaseEntity;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.model.type.GradeType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GradeType grade;

    @Embedded
    @Column(nullable = false)
    private Evaluation evaluation;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private Boolean operaGlass;

    @Column(nullable = false)
    private Boolean block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musical_id")
    private Musical musical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private TheaterSeat theaterSeat;

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewFile> reviewFiles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewTag> reviewTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewHeart> reviewHearts = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public static Review of(TheaterSeat theaterSeat, Musical musical, ReviewRequestDto requestDto) {
        Evaluation evaluation = Evaluation.of(requestDto);

        boolean operaGlass = requestDto.getOperaGlass() != null && requestDto.getOperaGlass().equals("on");
        boolean blockSight = requestDto.getBlock() != null && requestDto.getBlock().equals("on");

        return Review.builder()
                .content(requestDto.getReviewContent())
                .grade((requestDto.getGrade()))
                .evaluation(evaluation)
                .score(calculateScore(evaluation))
                .operaGlass(operaGlass)
                .block(blockSight)
                .musical(musical)
                .theaterSeat(theaterSeat)
                .build();
    }

    public void addFiles(List<ReviewFile> files) {
        this.reviewFiles.clear();
        this.reviewFiles.addAll(files);
    }

    public void addTags(List<ReviewTag> tags) {
        this.reviewTags.clear();
        this.reviewTags.addAll(tags);
    }

    private static Double calculateScore(Evaluation evaluation) {
        Double score = 0.0;

        score += evaluation.getGap().getScore();
        score += evaluation.getLight().getScore();
        score += evaluation.getSight().getScore();
        score += evaluation.getSound().getScore();

        // 0 ~ 10
        score = (score - 4) / 8 * 10;
        // 0.5 단위 절삭
        return score = Math.ceil(score * 2) / 2;
    }

    public void update(TheaterSeat theaterSeat, Musical musical, ReviewRequestDto requestDto) {
        Evaluation evaluation = Evaluation.of(requestDto);

        boolean operaGlass = requestDto.getOperaGlass() != null && requestDto.getOperaGlass().equals("on");
        boolean blockSight = requestDto.getBlock() != null && requestDto.getBlock().equals("on");

        this.content = requestDto.getReviewContent();
        this.grade = (requestDto.getGrade());
        this.evaluation = evaluation;
        this.score = calculateScore(evaluation);
        this.operaGlass = operaGlass;
        this.block = blockSight;
        this.musical = musical;
        this.theaterSeat = theaterSeat;
    }
}
