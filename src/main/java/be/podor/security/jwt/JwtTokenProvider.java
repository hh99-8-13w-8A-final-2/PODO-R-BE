package be.podor.security.jwt;

import be.podor.member.model.Member;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.refresh.RefreshToken;
import be.podor.security.jwt.refresh.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    public static String BEARER_PREFIX = "Bearer ";

    // 토큰 유효시간
    // 프론트엔드와 약속해야 함
    private final Long TokenValidTime = 30 * 60 * 1000L;  // 30분
    private final Long RefreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;  // 1주일

    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    private Key key;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        //       아스키코드로 바꿔서 바이트 배열을 생성
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//      hmacShaKeyFor 메소드를 이용하여 key 객체로 변경
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey,
                            UserDetailsService userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(Member member) {
        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + TokenValidTime);
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())// 유저 정보 Id값 저장
                .setAudience(member.getNickname()) // 유저 정보 닉네임값 저장
                .signWith(key, SignatureAlgorithm.HS256) // 키값과 알고리즘 세팅
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + RefreshTokenValidTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return TokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(BEARER_PREFIX + accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();

    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody().getSubject();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // 토큰 유효성 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
//                    jwt 서명 검증을 위한 secret key를 들고온다.
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
        return optionalRefreshToken.orElse(null);
    }

    @Transactional
    public ResponseEntity<?> deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        if (null == refreshToken) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("TOKEN_NOT_FOUND"));
        }

        refreshTokenRepository.delete(refreshToken);
        return ResponseEntity.ok("success");
    }

    public Member getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getMember();

    }
}