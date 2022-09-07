package be.podor.review.dto;

import be.podor.review.model.reviewInfo.ScoreEnum;
import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.type.GradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private GradeType grade;
    private FloorType floor;
    private String section;
    private String row;
    private Integer seat;
    private String reviewContent;
    private List<String> imgUrls;
    private ScoreEnum gap;
    private ScoreEnum sight;
    private ScoreEnum sound;
    private ScoreEnum light;
    private String operaGlass;
    private String block;
    private String tags;
    // Todo member @CreatedBy
}
