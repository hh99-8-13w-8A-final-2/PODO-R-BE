package be.podor.review.dto;

import be.podor.review.model.reviewInfo.ScoreEnum;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchDto {
    private ScoreEnum gap;
    private ScoreEnum sight;
    private ScoreEnum sound;
    private ScoreEnum light;
    private Boolean block;
    private Boolean operaGlass;
    private Set<String> tags;

    public static SearchDto of(SearchRequestParam requestParam) {
        Set<String> evaluationSet = requestParam.getEvaluation();
        Set<String> tagSet = requestParam.getTag();

        return SearchDto.builder()
                .gap(parseScoreEnum(evaluationSet, "단차좋음"))
                .sight(parseScoreEnum(evaluationSet, "시야좋음"))
                .sound(parseScoreEnum(evaluationSet, "음향좋음"))
                .light(parseScoreEnum(evaluationSet, "조명좋음"))
                .block(parseBooleanValue(tagSet, "#시야방해있음"))
                .operaGlass(parseBooleanValue(tagSet, "#오페라글라스필수"))
                .tags(tagSet)
                .build();
    }

    private static ScoreEnum parseScoreEnum(Set<String> evaluationSet, String score) {
        if (evaluationSet != null && evaluationSet.contains(score)) {
            evaluationSet.remove(score);
            return ScoreEnum.GOOD;
        }
        return null;
    }

    private static Boolean parseBooleanValue(Set<String> tagSet, String booleanTag) {
        if (tagSet != null && tagSet.contains(booleanTag)) {
            tagSet.remove(booleanTag);
            return true;
        }
        return null;
    }
}
