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
        Set<String> tagSet = requestParam.getTag();
        return SearchDto.builder()
                .gap(ScoreEnum.from(requestParam.getGap()))
                .sight(ScoreEnum.from(requestParam.getSight()))
                .sound(ScoreEnum.from(requestParam.getSound()))
                .light(ScoreEnum.from(requestParam.getLight()))
                .block(parseBooleanValue(tagSet, "#시야방해있음"))
                .operaGlass(parseBooleanValue(tagSet, "#오페라글라스필수"))
                .tags(tagSet)
                .build();
    }

    private static Boolean parseBooleanValue(Set<String> tagSet, String booleanTag) {
        if (tagSet.contains(booleanTag)) {
            tagSet.remove(booleanTag);
            return true;
        }
        return null;
    }
}
