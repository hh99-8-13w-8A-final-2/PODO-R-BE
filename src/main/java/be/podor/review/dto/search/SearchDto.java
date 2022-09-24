package be.podor.review.dto.search;

import be.podor.review.model.reviewInfo.ScoreEnum;
import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.type.GradeType;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchDto {
    private GradeType grade;

    private FloorType floor;
    private String section;
    private String row;
    private Integer seat;

    private ScoreEnum gap;
    private ScoreEnum sight;
    private ScoreEnum sound;
    private ScoreEnum light;

    private String search;

    private Boolean block;
    private Boolean operaGlass;

    private Set<String> tags;

    public static SearchDto of(SearchRequestParam requestParam) {
        Set<String> evaluationSet = requestParam.getEvaluation();
        Set<String> tagSet = requestParam.getTag();

        return SearchDto.builder()
                .grade(GradeType.from(requestParam.getGrade()))

                .floor(FloorType.from(requestParam.getFloor()))
                .section(requestParam.getSection())
                .row(requestParam.getRow())
                .seat(requestParam.getSeat())

                .gap(parseScoreEnum(evaluationSet, "단차좋음"))
                .sight(parseScoreEnum(evaluationSet, "시야좋음"))
                .sound(parseScoreEnum(evaluationSet, "음향좋음"))
                .light(parseScoreEnum(evaluationSet, "조명좋음"))

                .search(requestParam.getSearch())

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
