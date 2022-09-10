package be.podor.member.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.dto.SocialUserDto;
import be.podor.member.dto.TwitterUserInfoDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.security.jwt.JwtTokenProvider;
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

    // 트위터 로그인
    public SocialUserDto twitterLogin(String oauthToken, String oauthVerifier) throws TwitterException {
        HttpParameter[] httpParameters = {
                new HttpParameter("oauth_token", oauthToken),
                new HttpParameter("oauth_verifier", oauthVerifier)
        };

        HttpResponse response = HttpClientFactory.getInstance().post("https://api.twitter.com/oauth/access_token",
                httpParameters,
                null,
                null);

        String tokenString = response.asString();

        String[] split = tokenString.split("&");
        String token = split[0].split("=")[1];
        String tokenSecret = split[1].split("=")[1];

        ConfigurationBuilder twitterConf = new ConfigurationBuilder();
        twitterConf.setDebugEnabled(true);
        twitterConf.setOAuthConsumerKey(consumerKey);
        twitterConf.setOAuthConsumerSecret(consumerSecret);
        twitterConf.setOAuthAccessToken(token);
        twitterConf.setOAuthAccessTokenSecret(tokenSecret);

        User twitterUser = new TwitterFactory(twitterConf.build())
                .getInstance()
                .verifyCredentials();

        Member twitterMember = signupTwitterUser(TwitterUserInfoDto.of(twitterUser));

        TokenDto tokenDto = jwtTokenProvider.createToken(twitterMember);

        MemberDto memberDto = new MemberDto(twitterMember.getId(), twitterMember.getNickname(), twitterMember.getProfilePic());

        SocialUserDto socialUserDto = new SocialUserDto(memberDto, tokenDto);

        return socialUserDto;
    }

    Member signupTwitterUser(TwitterUserInfoDto twitterUserInfoDto) {
        Long twitterId = twitterUserInfoDto.getTwitterId();
        Member findKakao = memberRepository.findByTwitterId(twitterId.toString())
                .orElseGet(() -> {
                    Member kakaoUser = Member.of(twitterUserInfoDto);
                    return memberRepository.save(kakaoUser);
                });
        return findKakao;
    }
}
