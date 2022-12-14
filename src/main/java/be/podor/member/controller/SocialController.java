package be.podor.member.controller;


import be.podor.member.dto.MemberDto;
import be.podor.member.dto.MemberInfoRequestDto;
import be.podor.member.dto.SocialUserDto;
import be.podor.member.service.KakaoService;
import be.podor.member.service.MemberService;
import be.podor.member.service.TwitterService;
import be.podor.member.util.MemberUtil;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
                .headers(MemberUtil.getTokenHeaders(socialUserDto.getTokenDto()))
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
                .headers(MemberUtil.getTokenHeaders(socialUserDto.getTokenDto()))
                .body(socialUserDto.getMemberDto());
    }

    @PostMapping("/member/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.logout(userDetails);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        TokenDto tokenDto = memberService.reissue(request);
        return ResponseEntity.ok()
                .headers(MemberUtil.getTokenHeaders(tokenDto))
                .build();
    }

    @PutMapping("/member/update")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid @RequestBody MemberInfoRequestDto requestDto) {
        MemberDto memberDto = memberService.updateMemberInfo(userDetails, requestDto);
        return ResponseEntity.ok(memberDto);
    }

    @DeleteMapping("/member/delete")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.deleteMember(userDetails);
        return ResponseEntity.ok().build();
    }
}



