package be.podor.member.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.member.dto.MemberInfoRequestDto;
import be.podor.member.validator.MemberValidator;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.JwtTokenProvider;
import be.podor.security.jwt.TokenDto;
import be.podor.security.jwt.refresh.RefreshToken;
import be.podor.security.jwt.refresh.RefreshTokenRepository;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import static be.podor.security.jwt.JwtFilter.REFRESH_TOKEN_HEADER;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public void logout(UserDetailsImpl userDetails) {
        refreshTokenRepository.deleteByMember_Id(userDetails.getMemberId());
    }

    public TokenDto saveToken(Member member) {

        TokenDto tokenDto = jwtTokenProvider.createToken(member);

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .tokenValue(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(HttpServletRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByTokenValue(request.getHeader(REFRESH_TOKEN_HEADER))
                .orElseThrow(() -> new UnsupportedJwtException("로그인 정보 갱신에 실패하여 로그아웃 되었습니다."));//403);

        TokenDto newToken = jwtTokenProvider.createToken(refreshToken.getMember());

        refreshToken.updateToken(newToken.getRefreshToken());

        return newToken;
    }

    @Transactional
    public MemberDto updateMemberInfo(UserDetailsImpl userDetails, MemberInfoRequestDto requestDto) {
        Member member = MemberValidator.validate(memberRepository, userDetails.getMemberId());
        member.updateMember(requestDto);
        return MemberDto.of(member);
    }
}
