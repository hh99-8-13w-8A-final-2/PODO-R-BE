package be.podor.review.dto;

import be.podor.review.model.reviewInfo.ScoreEnum;
import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.type.GradeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @NotNull
    private GradeType grade;

    @NotNull
    private FloorType floor;

    @NotNull
    private String section;

    @NotNull
    private String row;

    @NotNull
    private Integer seat;

    @NotNull
    @NotBlank
    private String reviewContent;

    @Size(min = 1, max = 4)
    private List<String> imgUrls;

    @NotNull
    private ScoreEnum gap;

    @NotNull
    private ScoreEnum sight;

    @NotNull
    private ScoreEnum sound;

    @NotNull
    private ScoreEnum light;

    private String operaGlass;

    private String block;

    private String tags;
}
