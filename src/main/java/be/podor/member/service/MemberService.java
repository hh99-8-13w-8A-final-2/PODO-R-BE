package be.podor.member.service;

import be.podor.member.model.Member;
import be.podor.security.jwt.JwtTokenProvider;
import be.podor.security.jwt.TokenDto;
import be.podor.security.jwt.refresh.RefreshToken;
import be.podor.security.jwt.refresh.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void logout(Member member) {
        refreshTokenRepository.deleteByMember(member);
    }

    public TokenDto saveToken(Member member) {

        TokenDto tokenDto = jwtTokenProvider.createToken(member);

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return tokenDto;
    }
}
