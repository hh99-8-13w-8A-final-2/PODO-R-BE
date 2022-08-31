package be.podor.review.model.reviewInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ScoreEnum from(Integer score) {
        for (ScoreEnum scoreEnum : ScoreEnum.values()) {
            if (scoreEnum.getScore().equals(score)) {
                return scoreEnum;
            }
        }
        return null;
    }
}
