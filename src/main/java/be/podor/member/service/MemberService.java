package be.podor.member.service;

import be.podor.member.model.Member;
import be.podor.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> logout(HttpServletRequest request) {
        Member member = jwtTokenProvider.getMemberFromAuthentication();
        System.out.println(jwtTokenProvider.getMemberFromAuthentication());
        if (!jwtTokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("INVALID_TOKEN"));
        }
        if (null == member) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error("MEMBER_NOT_FOUND"));
        }
        return ResponseEntity.ok().body(jwtTokenProvider.deleteRefreshToken(member));
    }
}
