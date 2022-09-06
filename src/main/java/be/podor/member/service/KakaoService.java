package be.podor.member.service;


import be.podor.member.dto.KakaoUserInfoDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.JwtTokenProvider;
import be.podor.security.jwt.TokenDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class KakaoService {
    @Value("${kakao.client-id}")
    String kakaoClientId;
    @Value("${kakao.redirect-uri}")
    String redirectUri;

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenDto kakaoLogin(String code)
            throws IOException {
        // 1. "인가코드" 로 "액세스 토큰" 요청
        String accessToken = getKakaoAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 카카오ID로 회원가입 처리
        Member kakaoUser = signupKakaoUser(kakaoUserInfo);

        //4. 강제 로그인 처리
        forceLoginKakaoUser(kakaoUser);

        return jwtTokenProvider.createToken(kakaoUser);
    }

    //header 에 Content-type 지정
    //1번
    String getKakaoAccessToken(String code) throws IOException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );//리스폰스 받기

        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody(); //바디부분
        ObjectMapper objectMapper = new ObjectMapper(); //오브젝트 맵퍼 생성
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    //2번
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws IOException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        //HTTP 응답 (JSON)
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String profileUrl = jsonNode.get("properties")
                .get("profile_image").asText();
        return new KakaoUserInfoDto(id, nickname, profileUrl);
    }

    // 3번
    Member signupKakaoUser(KakaoUserInfoDto kakaoUserInfoDto) {
        // 재가입 방지
//        int mannerTemp = userRoleCheckService.userResignCheck(kakaoUserInfoDto.getEmail());
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfoDto.getKakaoId();
        Member findKakao = memberRepository.findByKakaoId(kakaoId)
                //DB에 중복된 계정이 없으면 회원가입 처리
                .orElseGet(() -> {
                    Member kakaoUser = Member.of(kakaoUserInfoDto);
                    return memberRepository.save(kakaoUser);
                });
        return findKakao;
    }

    // 4번
    public void forceLoginKakaoUser(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
