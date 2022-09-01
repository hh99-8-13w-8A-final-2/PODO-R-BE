package be.podor.member.controller;


import be.podor.member.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(value = "code") String code) throws IOException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code));
    }
}



