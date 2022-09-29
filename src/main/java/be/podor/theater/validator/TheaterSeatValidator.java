package be.podor.theater.validator;

import be.podor.exception.podor.PodoalException;
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
        ).orElseThrow(() -> PodoalException.NO_THEATER_SEAT_EXCEPTION);

        return theaterSeat;
    }
}
