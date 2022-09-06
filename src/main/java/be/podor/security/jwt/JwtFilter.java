package be.podor.security.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
// OncePerRequestFilter = 사용자의 요청 한번에 한번만 실행되는 필터를 생성한다 = "필터결과를 재활용 한다"
public class JwtFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
//    ---------------------------------------------------------------------


    //    security context에 저장하여 사용
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


//       토큰 선언
        String jwt = resolveToken(request); // 변수이름 수정요망

//      hasText = 문자가 유효한지 체크하는 메소드 (공백을 제외하고 길이가 1이상인경우 true 값을 내보냄)
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    //    토큰 추출 메소드
    private String resolveToken(HttpServletRequest request) {
        String Token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(Token) && Token.startsWith(BEARER_PREFIX)) {
            return Token.substring(7);
        }
        return null;
    }
}
