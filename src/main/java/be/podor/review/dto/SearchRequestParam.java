package be.podor.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchRequestParam {
    private Integer gap;
    private Integer sight;
    private Integer sound;
    private Integer light;
    private Set<String> tag;
}
