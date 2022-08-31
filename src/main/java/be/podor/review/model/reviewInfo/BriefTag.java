package be.podor.review.model.reviewInfo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BriefTag {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScoreEnum gap;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScoreEnum sight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScoreEnum light;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScoreEnum sound;
}

