package be.podor.member.controller;


import be.podor.member.dto.SocialUserDto;
import be.podor.member.model.Member;
import be.podor.member.service.KakaoService;
import be.podor.member.service.MemberService;
import be.podor.member.service.TwitterService;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final MemberService memberService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam(value = "code") String code) throws IOException {
        SocialUserDto socialUserDto = kakaoService.kakaoLogin(code);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, socialUserDto.getTokenDto().getAccessToken());
        headers.add("RefreshToken", socialUserDto.getTokenDto().getRefreshToken());
        return ResponseEntity.ok().headers(headers).body(socialUserDto.getMemberDto());
    }

    @GetMapping("/oauth/twitter")
    public ResponseEntity<?> twitterLogin(
            @RequestParam(value = "code") String code) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.ok(twitterService.twitterLogin(code));
    }

    @PostMapping("/member/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = ((UserDetailsImpl) userDetails).getMember();
        memberService.logout(member);
        return ResponseEntity.ok().build();
    }
}



