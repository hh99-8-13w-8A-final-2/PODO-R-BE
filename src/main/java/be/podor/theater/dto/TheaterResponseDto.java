package be.podor.theater.dto;

import be.podor.theater.model.Theater;
import be.podor.theater.model.TheaterConvenience;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<ConvenienceResponseDto> conveniences;
    private Double la;
    private Double lo;
    private String theaterSeatImage;

    public static TheaterResponseDto of(Theater theater) {
        List<ConvenienceResponseDto> convenienceCodes = theater.getTheaterConveniences().stream()
                .map(TheaterConvenience::getConvenience)
                .map(ConvenienceResponseDto::of)
                .collect(Collectors.toList());

        return TheaterResponseDto.builder()
                .theaterId(theater.getTheaterId())
                .theaterName(theater.getTheaterName())
                .theaterTel(theater.getTheaterTel())
                .theaterUrl(theater.getTheaterUrl())
                .theaterAddr(theater.getTheaterAddr())
                .conveniences(convenienceCodes)
                .la(theater.getLa())
                .lo(theater.getLo())
                .theaterSeatImage(theater.getTheaterSeatImage())
                .build();
    }
}
