package be.podor.review.model.reviewfile;

import be.podor.review.model.Review;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewFile of(String filePath, Review review) {
        return ReviewFile.builder()
                .filePath(filePath)
                .review(review)
                .build();
    }
}
