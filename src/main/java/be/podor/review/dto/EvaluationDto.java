package be.podor.review.dto;

import be.podor.review.model.reviewInfo.Evaluation;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EvaluationDto {
    private String gap;
    private String sight;
    private String sound;
    private String light;

    public static EvaluationDto of(Evaluation evaluation) {
        return EvaluationDto.builder()
                .gap(evaluation.getGap().toString())
                .sight(evaluation.getSight().toString())
                .sound(evaluation.getSound().toString())
                .light(evaluation.getLight().toString())
                .build();
    }
}
