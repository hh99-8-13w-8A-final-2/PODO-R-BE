package be.podor.security.jwt;

import be.podor.member.model.Member;
import be.podor.security.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    public static String BEARER_PREFIX = "Bearer ";

    private final Long TokenValidTime = 60 * 60 * 1000L;  // 60분
    private final Long RefreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;  // 1주일

    private final UserDetailsService userDetailsService;


    private Key key;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    public TokenDto createToken(Member member) {
        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + TokenValidTime);
        Date refreshTokenExpiresIn = new Date(now + RefreshTokenValidTime);

        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())// 유저 정보 Id값 저장
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256) // 키값과 알고리즘 세팅
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        return TokenDto.builder()
                .accessToken(BEARER_PREFIX + accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String jwtToken) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(getUserPk(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public boolean validateToken(String jwtToken) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken);
        return true;
    }

}