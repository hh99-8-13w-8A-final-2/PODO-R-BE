package be.podor.review.model.reviewfile;

import be.podor.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
}
