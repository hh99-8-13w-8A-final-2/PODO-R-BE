package be.podor.review.dto;

import be.podor.review.model.reviewInfo.ScoreEnum;
import lombok.*;

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
    private String[] tags;

    public static SearchDto of(SearchRequestParam requestParam) {
        return SearchDto.builder()
                .gap(ScoreEnum.from(requestParam.getGap()))
                .sight(ScoreEnum.from(requestParam.getSight()))
                .sound(ScoreEnum.from(requestParam.getSound()))
                .light(ScoreEnum.from(requestParam.getLight()))
                .block(parseBooleanValue(requestParam.getBlock()))
                .operaGlass(parseBooleanValue(requestParam.getOperaGlass()))
                .tags(requestParam.getTag())
                .build();
    }

    private static Boolean parseBooleanValue(Integer integer) {
        if (integer != null) {
            return integer != 0 ? true : null;
        }
        return null;
    }
}
