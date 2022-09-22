package be.podor.member.controller;


import be.podor.member.dto.SocialUserDto;
import be.podor.member.service.KakaoService;
import be.podor.member.service.MemberService;
import be.podor.member.service.TwitterService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialController {
    private final KakaoService kakaoService;
    private final TwitterService twitterService;
    private final MemberService memberService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(value = "code") String code) throws IOException {
        SocialUserDto socialUserDto = kakaoService.kakaoLogin(code);
        return ResponseEntity.ok()
                .headers(socialUserDto.getTokenHeaders())
                .body(socialUserDto.getMemberDto());
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

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.reissue(request));
    }
}



