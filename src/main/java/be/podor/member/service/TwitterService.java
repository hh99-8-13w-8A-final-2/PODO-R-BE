package be.podor.member.service;

import antlr.MismatchedTokenException;
import be.podor.member.dto.TwitterUserInfoDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.security.UserDetailsImpl;
import be.podor.security.jwt.JwtTokenProvider;
import be.podor.security.jwt.TokenDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

@Service
@RequiredArgsConstructor
public class TwitterService {
    @Value("${twitter.client-id}")
    String consumer_KEY;

    @Value("${twitter.redirect-uri}")
    String twitterRedirectUri;

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 url 리턴
    public String getTwitterAuthorizationURL() throws TwitterException {
        ConfigurationBuilder twitterConf = new ConfigurationBuilder();
        twitterConf.setDebugEnabled(true);
        twitterConf.setOAuthConsumerKey(consumerKey);
        twitterConf.setOAuthConsumerSecret(consumerSecret);

        Twitter twitter = new TwitterFactory(twitterConf.build()).getInstance();

        RequestToken requestToken = twitter.getOAuthRequestToken(twitterRedirectUri);

        return requestToken.getAuthorizationURL();
    }

    private HttpHeaders getHeaders() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, getAuthHeader());
        return headers;
    }

    private String getAuthHeader() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String randomNumber = String.valueOf(new Random());
        String oauthNonce = randomNumber;
        Long oauthTimestamp = Instant.now().getEpochSecond();
        String baseString = "POST&" +
                URLEncoder.encode(URL, StandardCharsets.UTF_8.toString()) + "&" +
                URLEncoder.encode(
                        (
                                "&oauth_consumer_key=" + consumer_KEY
                                        + "&oauth_nonce=" + oauthNonce
                                        + "&oauth_signature_method=" + "HMAC-SHA1"
                                        + "&oauth_timestamp=" + oauthTimestamp
                                        + "&oauth_token=" + MismatchedTokenException.TOKEN
                                        + "&oauth_version= 1.0"
                        ), StandardCharsets.UTF_8.toString()
                );
        String sigString = URLEncoder.encode(consumer_secret, StandardCharsets.UTF_8.toString())
                + "&" + URLEncoder.encode(tokenSecret, StandardCharsets.UTF_8.toString());
        String signature = generateSignature(baseString, sigString, "HmacSHA1");
        String headers =
                "oauth_consumer_key=\"" + URLEncoder.encode(
                        consumer_KEY, StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_token=\"" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_signature_method=\"" + URLEncoder.encode(
                        "HMAC-SHA1", StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_timestamp=\"" + URLEncoder.encode(
                        String.valueOf(oauthTimestamp).substring(0, 10), StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_nonce=\"" + URLEncoder.encode(oauthNonce, StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_version=\"" + URLEncoder.encode(
                        "1.0", StandardCharsets.UTF_8.toString()) + "\", "
                        + "oauth_signature=\"" + URLEncoder.encode(signature, StandardCharsets.UTF_8.toString()) + "\" ";
        return headers;
    }

    public static String generateSignature(String msg, String keyString, String algoritmo) throws InvalidKeyException,
            NoSuchAlgorithmException {
        String digest;

        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), algoritmo);
        Mac mac = Mac.getInstance(algoritmo);
        mac.init(key);
        byte[] bytes = mac.doFinal(msg.getBytes(StandardCharsets.US_ASCII));
        digest = Base64.getEncoder().encodeToString(bytes);

        return digest;
    }

    private String getTwitterAccessToken(String code) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        // HTTP Header 생성 //getheaders
        HttpHeaders headers = getHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", consumer_KEY);
        body.add("redirect_uri", twitterRedirectUri);
        body.add("code", code);

        //HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> twitterTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.twitter.com/oauth",
                HttpMethod.POST,
                twitterTokenRequest,
                String.class
        );//리스폰스 받기

        //HTTP 응답 (JSON) -> 액세스 토큰 파싱
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody(); //바디부분
        ObjectMapper objectMapper = new ObjectMapper(); //오브젝트 맵퍼 생성
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public TwitterUserInfoDto getTwitterUserInfo(String accessToken) throws IOException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> twitterUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.twitter.com/2/users",
                HttpMethod.GET,
                twitterUserInfoRequest,
                String.class
        );
        //HTTP 응답 (JSON)
        //JSON -> JsonNode 객체로 변환
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("name").asText();
        String profileUrl = jsonNode.get("propile_image_url").asText();
        return new TwitterUserInfoDto(id, nickname, profileUrl);
    }

    private Member signupTwitterUser(TwitterUserInfoDto twitterUserInfoDto) {
        // 재가입 방지
//        int mannerTemp = userRoleCheckService.userResignCheck(kakaoUserInfoDto.getEmail());
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String twitterId = twitterUserInfoDto.getTwitterId();
        Member findTwitter = memberRepository.findByTwitterId(twitterId)
                //DB에 중복된 계정이 없으면 회원가입 처리
                .orElseGet(() -> {
                    Member twitterUser = Member.of(twitterUserInfoDto);
                    return memberRepository.save(twitterUser);
                });
        return findTwitter;
    }
}
