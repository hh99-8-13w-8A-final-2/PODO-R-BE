package be.podor.member.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long KakaoId;
    private String nickname;
    private String profilePic;

}
