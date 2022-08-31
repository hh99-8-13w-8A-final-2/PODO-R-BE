package be.podor.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
import java.security.Key;
import java.time.Instant;

@RequiredArgsConstructor
// OncePerRequestFilter = 사용자의 요청 한번에 한번만 실행되는 필터를 생성한다 = "필터결과를 재활용 한다"
public class JwtFilter extends OncePerRequestFilter {

    // Header에 들어갈 엑세스토큰 키값을 상수 선언한다.
    public static String AUTHORIZATION_HEADER = "Authorization";
    // Header에 들어갈 엑세스 토큰 앞에 붙을 값을 상수 선언한다.
    public static String BEARER_PREFIX = "Bearer ";
    //   PAYLOAD 분해시 필요한 키값을 넣는다.

    public static String AUTHORITIES_KEY = "auth";
    //    new JwtFilter 생성자에 넣은 변수값을 불러온다. ---------------------------------

    private final String secretKey;
    private final JwtTokenProvider jwtTokenProvider;
//    ---------------------------------------------------------------------


    //    security context에 저장하여 사용
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//       아스키코드로 바꿔서 바이트 배열을 생성
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

//      hmacShaKeyFor 메소드를 이용하여 key 객체로 변경
        Key key = Keys.hmacShaKeyFor(keyBytes);

//       토큰 선언
        String jwt = subToken(request);

//      hasText = 문자가 유효한지 체크하는 메소드 (공백을 제외하고 길이가 1이상인경우 true 값을 내보냄)
        if (!(subToken(request) == null)) {
            if (StringUtils.hasText(jwt) && jwtTokenProvider.CheckToken(request)) {
                //매니저님 주석 -- Payload 부분에는 토큰에 담을 정보가 들어있습니다. 여기에 담는 정보의 한 ‘조각’ 을 클레임(claim) 이라고 부름
                //매니저님 주석 -- name / value 의 한 쌍으로 이뤄져있음
                Claims claims;
                try {
                    claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                } catch (ExpiredJwtException e) {
                    claims = e.getClaims();
                }

                if (claims.getExpiration().toInstant().toEpochMilli() < Instant.now().toEpochMilli()) {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(
//                            new ObjectMapper().writeValueAsString(
//                                    ResponseDto.fail("BAD_REQUEST", "Token이 유효하지 않습니다.")
//                            )
                    );
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    //SC_BAD_REQUEST = 400

                }

                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }


        filterChain.doFilter(request, response);
    }

    //    토큰 추출 메소드
    private String subToken(HttpServletRequest request) {
        String Token = request.getHeader(AUTHORIZATION_HEADER);
//        Token 값이 존재하고 선언한 상수로 시작하면 토큰값을 가져감
        if (StringUtils.hasText(Token) && Token.startsWith(BEARER_PREFIX)) {
            return Token.substring(7);
        }
        return null;
    }
}
