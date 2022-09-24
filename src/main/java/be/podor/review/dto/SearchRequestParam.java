package be.podor.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchRequestParam {
    private String grade;
    private String floor;
    private String section;
    private String row;
    private Integer seat;
    private Set<String> evaluation;
    private Set<String> tag;
}
