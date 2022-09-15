package be.podor.musical.dto;

import be.podor.musical.model.Musical;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicalListResponseDto {
    private Long musicalId;
    private String musicalName;
    private String musicalRegion;
    private String musicalTheater;
    private String musicalPoster;
    private String musicalPosterWide;
    private LocalDate openDate;
    private LocalDate closeDate;

    public static MusicalListResponseDto of(Musical musical) {
        return MusicalListResponseDto.builder()
                .musicalId(musical.getMusicalId())
                .musicalName(musical.getMusicalName())
                .musicalRegion(musical.getTheater().getTheaterAddr())
                .musicalTheater(musical.getTheater().getTheaterName())
                .musicalPoster(musical.getMusicalPoster())
                .musicalPosterWide(musical.getMusicalPosterWide())
                .openDate(musical.getOpenDate())
                .closeDate(musical.getCloseDate())
                .build();
    }
}
