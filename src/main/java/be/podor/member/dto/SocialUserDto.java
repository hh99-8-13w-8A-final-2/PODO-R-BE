package be.podor.member.dto;


import be.podor.security.jwt.JwtFilter;
import be.podor.security.jwt.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserDto {
    private MemberDto memberDto;
    private TokenDto tokenDto;

    public HttpHeaders getTokenHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtFilter.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
        headers.add(JwtFilter.REFRESH_TOKEN_HEADER, tokenDto.getRefreshToken());

        return headers;
    }
}
