package be.podor.member.model;

import be.podor.member.dto.KakaoUserInfoDto;
import be.podor.member.dto.TwitterUserInfoDto;
import be.podor.mypage.dto.MyPageRequestDto;
import be.podor.share.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private Long twitterId;

    @Column
    private String nickname;

    @Column
    private String profilePic;


    public void updateMember(MyPageRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }

    public static Member of(KakaoUserInfoDto kakaoUserInfoDto) {

        return Member.builder()
                .nickname(kakaoUserInfoDto.getNickname())
                .profilePic(kakaoUserInfoDto.getProfilePic())
                .kakaoId(kakaoUserInfoDto.getKakaoId())
                .build();
    }

    public static Member of(TwitterUserInfoDto twitterUserInfoDto) {

        return Member.builder()
                .nickname(twitterUserInfoDto.getNickname())
                .profilePic(twitterUserInfoDto.getProfilePic())
                .twitterId(twitterUserInfoDto.getTwitterId())
                .build();
    }
}
