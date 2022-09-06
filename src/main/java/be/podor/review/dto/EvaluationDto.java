package be.podor.review.dto;

import be.podor.review.model.reviewInfo.Evaluation;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationDto {
    private Integer gap;
    private Integer sight;
    private Integer sound;
    private Integer light;

    public static EvaluationDto of(Evaluation evaluation) {
        return EvaluationDto.builder()
                .gap(evaluation.getGap().getScore())
                .sight(evaluation.getSight().getScore())
                .sound(evaluation.getSound().getScore())
                .light(evaluation.getLight().getScore())
                .build();
    }
}
