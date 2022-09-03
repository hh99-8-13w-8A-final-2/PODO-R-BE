package be.podor.theater.validator;

import be.podor.musical.model.Musical;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterSeatRepository;

public class TheaterSeatValidator {

    public static TheaterSeat validate(TheaterSeatRepository theaterSeatRepository, ReviewRequestDto requestDto, Musical musical) {
        String section = requestDto.getSection();
        if (section == null || section.isEmpty()) {
            section = null;
        }

        TheaterSeat theaterSeat = theaterSeatRepository.findByFloorAndSectionAndSeatRowAndSeatAndTheater_TheaterId(
                requestDto.getFloor(),
                section,
                requestDto.getRow(),
                requestDto.getSeat(),
                musical.getTheater().getTheaterId()
        ).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 좌석입니다.")
        );

        return theaterSeat;
    }
}
