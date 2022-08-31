package be.podor.theater.controller;

import be.podor.theater.dto.TheaterResponseDto;
import be.podor.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    //극장 정보 조회
    @GetMapping("/api/theaters/{theaterId}")
    public ResponseEntity<?> findTheater(@PathVariable Long theaterId) {
        TheaterResponseDto responseDto = theaterService.findTheater(theaterId);

        return ResponseEntity.ok(responseDto);
    }
}
