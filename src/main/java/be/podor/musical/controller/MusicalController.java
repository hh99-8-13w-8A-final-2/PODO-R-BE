package be.podor.musical.controller;

import be.podor.musical.dto.MusicalListResponseDto;
import be.podor.musical.service.MusicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicalController {

    private final MusicalService musicalService;

    // 메인화면 상영중 뮤지컬 가져오기
    @GetMapping("/api/musicals/open")
    public ResponseEntity<?> getOpenMusical() {
        List<MusicalListResponseDto> responseDto = musicalService.getOpenMusical();

        return ResponseEntity.ok(responseDto);
    }
}
