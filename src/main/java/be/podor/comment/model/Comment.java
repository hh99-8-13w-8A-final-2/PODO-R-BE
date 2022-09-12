package be.podor.comment.model;

import be.podor.comment.dto.CommentRequestDto;
import be.podor.review.model.Review;
import be.podor.share.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static Comment of(CommentRequestDto requestDto, Review review) {
        return Comment.builder()
                .content(requestDto.getContent())
                .review(review)
                .build();
    }
}
