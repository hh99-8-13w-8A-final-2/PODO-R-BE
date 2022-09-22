package be.podor.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchRequestParam {
    private Set<String> evaluation;
    private Set<String> tag;
}
