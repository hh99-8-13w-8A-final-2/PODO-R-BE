package be.podor.member.controller;


import be.podor.member.dto.SocialUserDto;
import be.podor.member.service.KakaoService;
import be.podor.member.service.MemberService;
import be.podor.member.service.TwitterService;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.JwtFilter;
import be.podor.security.jwt.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.io.IOException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialController {
    private final KakaoService kakaoService;
    private final TwitterService twitterService;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(value = "code") String code) throws IOException {
        SocialUserDto socialUserDto = kakaoService.kakaoLogin(code);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, socialUserDto.getTokenDto().getAccessToken());
        headers.add("RefreshToken", socialUserDto.getTokenDto().getRefreshToken());
        return ResponseEntity.ok().headers(headers).body(socialUserDto.getMemberDto());
    }

    // 트위터 로그인 url 전달
    @GetMapping("/twitter/login")
    public ResponseEntity<?> getTwitterToken() throws TwitterException {
        return ResponseEntity.ok(twitterService.getTwitterAuthorizationURL());
    }

    // 트위터 로그인 수행
    @GetMapping("/oauth/twitter")
    public ResponseEntity<?> twitterLogin(
            @RequestParam("oauth_token") String oauthToken,
            @RequestParam("oauth_verifier") String oauthVerifier
    ) throws TwitterException {
        SocialUserDto socialUserDto = twitterService.twitterLogin(oauthToken, oauthVerifier);

        return ResponseEntity.ok()
                .headers(socialUserDto.getTokenHeaders())
                .body(socialUserDto.getMemberDto());
    }

    @PostMapping("/member/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.logout(userDetails);
        return ResponseEntity.ok().build();
    }
}



