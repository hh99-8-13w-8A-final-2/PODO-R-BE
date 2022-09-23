package be.podor.member.util;

import be.podor.security.jwt.JwtFilter;
import be.podor.security.jwt.TokenDto;
import org.springframework.http.HttpHeaders;

public class MemberUtil {
    public static HttpHeaders getTokenHeaders(TokenDto tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        headers.add(JwtFilter.REFRESH_TOKEN_HEADER, tokenDto.getRefreshToken());

        return headers;
    }
}
