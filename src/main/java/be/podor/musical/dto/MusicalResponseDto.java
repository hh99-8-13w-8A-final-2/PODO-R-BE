package be.podor.musical.dto;

import be.podor.musical.model.Musical;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MusicalResponseDto {

    private Long musicalId;

    private String musicalName;

    private String musicalPoster;

    private LocalDate openDate;

    private LocalDate closeDate;

    private Long theaterId;

    private String theaterName;

    public static MusicalResponseDto of(Musical musical) {
        return MusicalResponseDto.builder()
                .musicalId(musical.getMusicalId())
                .musicalName(musical.getMusicalName())
                .musicalPoster(musical.getMusicalPoster())
                .openDate(musical.getOpenDate())
                .closeDate(musical.getCloseDate())
                .theaterId(musical.getTheater().getTheaterId())
                .theaterName(musical.getTheater().getTheaterName())
                .build();
    }
}
