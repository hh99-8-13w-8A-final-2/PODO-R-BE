package be.podor.member.controller;


import be.podor.member.service.KakaoService;
import be.podor.member.service.TwitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialController {
    private final KakaoService kakaoService;
    private final TwitterService twitterService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(value = "code") String code) throws IOException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code));
    }

    @GetMapping("/oauth/twitter")
    public ResponseEntity<?> twitterLogin(
            @RequestParam(value = "code") String code) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.ok(twitterService.twitterLogin(code));
    }
}



