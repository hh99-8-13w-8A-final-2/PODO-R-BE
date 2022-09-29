package be.podor.member.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.dto.SocialUserDto;
import be.podor.member.dto.TwitterUserInfoDto;
import be.podor.member.model.Member;
import be.podor.member.model.MemberEnum;
import be.podor.member.repository.MemberRepository;
import be.podor.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

@Service
@RequiredArgsConstructor
public class TwitterService {
    @Value("${twitter.consumer-id}")
    String consumerKey;

    @Value("${twitter.consumer-secretkey}")
    String consumerSecret;

    @Value("${twitter.redirect-uri}")
    String twitterRedirectUri;

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    private final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";

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

    // 트위터 로그인
    public SocialUserDto twitterLogin(String oauthToken, String oauthVerifier) throws TwitterException {
        RequestToken requestToken = getTwitterRequestToken(oauthToken, oauthVerifier);

        ConfigurationBuilder twitterConf = new ConfigurationBuilder();
        twitterConf.setDebugEnabled(true);
        twitterConf.setOAuthConsumerKey(consumerKey);
        twitterConf.setOAuthConsumerSecret(consumerSecret);
        twitterConf.setOAuthAccessToken(requestToken.getToken());
        twitterConf.setOAuthAccessTokenSecret(requestToken.getTokenSecret());

        User twitterUser = new TwitterFactory(twitterConf.build())
                .getInstance()
                .verifyCredentials();

        Member twitterMember = signupTwitterUser(TwitterUserInfoDto.of(twitterUser));

        TokenDto tokenDto = memberService.saveToken(twitterMember);
        MemberDto memberDto = MemberDto.of(twitterMember);

        return new SocialUserDto(memberDto, tokenDto);
    }

    // 트위터 토큰 요청
    private RequestToken getTwitterRequestToken(String oauthToken, String oauthVerifier) throws TwitterException {
        HttpParameter[] httpParameters = {
                new HttpParameter("oauth_token", oauthToken),
                new HttpParameter("oauth_verifier", oauthVerifier)
        };

        HttpResponse response = HttpClientFactory.getInstance()
                .post(
                        ACCESS_TOKEN_URL,
                        httpParameters,
                        null,
                        null
                );

        String tokenString = response.asString();
        String[] split = tokenString.split("&");

        String token = split[0].split("=")[1];
        String tokenSecret = split[1].split("=")[1];

        return new RequestToken(token, tokenSecret);
    }

    Member signupTwitterUser(TwitterUserInfoDto twitterUserInfoDto) {
        Member twitterUser = memberRepository.findByTwitterId(twitterUserInfoDto.getTwitterId())
                .orElseGet(
                        () -> {
                            Member twitterMember = Member.of(twitterUserInfoDto);
                            twitterMember.setMemberRole(MemberEnum.ROLE_MEMBER);
                            return memberRepository.save(twitterMember);
                        }
                );
        return twitterUser;
    }
}
