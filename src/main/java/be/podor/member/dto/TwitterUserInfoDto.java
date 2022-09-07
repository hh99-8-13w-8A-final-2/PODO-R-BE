package be.podor.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TwitterUserInfoDto {
    private String twitterId;
    private String nickname;
    private String profilePic;
}
