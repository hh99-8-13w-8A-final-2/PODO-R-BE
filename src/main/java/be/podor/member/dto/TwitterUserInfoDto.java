package be.podor.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import twitter4j.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TwitterUserInfoDto {
    private Long twitterId;
    private String nickname;
    private String profilePic;

    public static TwitterUserInfoDto of(User user) {
        return TwitterUserInfoDto.builder()
                .twitterId(user.getId())
                .nickname(user.getName())
                .profilePic(user.getProfileImageURL())
                .build();
    }
}
