package be.podor.review.model.reviewInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScoreEnum {
    GOOD("상", 3),
    NORMAL("중", 2),
    BAD("하", 1);

    private final String text;
    private final Integer score;
}
