package be.podor.theater.service;

import be.podor.theater.dto.TheaterResponseDto;
import be.podor.theater.model.Theater;
import be.podor.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterResponseDto findTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 극장입니다.")
        );

        TheaterResponseDto responseDto = TheaterResponseDto.of(theater);

        return responseDto;
    }
}
