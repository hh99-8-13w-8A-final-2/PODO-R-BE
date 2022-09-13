package be.podor.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestParam {
    private Integer gap;
    private Integer sight;
    private Integer sound;
    private Integer light;
    private Integer block;
    private Integer operaGlass;
}
