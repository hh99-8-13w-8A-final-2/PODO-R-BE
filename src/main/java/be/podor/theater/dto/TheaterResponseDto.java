package be.podor.theater.dto;

import be.podor.theater.model.Theater;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class TheaterResponseDto {
    private Long theaterId;
    private String theaterName;
    private String theaterTel;
    private String theaterUrl;
    private String theaterAddr;
    private Double la;
    private Double lo;

    public static TheaterResponseDto of(Theater theater) {
        return TheaterResponseDto.builder()
                .theaterId(theater.getTheaterId())
                .theaterName(theater.getTheaterName())
                .theaterTel(theater.getTheaterTel())
                .theaterUrl(theater.getTheaterUrl())
                .theaterAddr(theater.getTheaterUrl())
                .la(theater.getLa())
                .lo(theater.getLo())
                .build();
    }
}
