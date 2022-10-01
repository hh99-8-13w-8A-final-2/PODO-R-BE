package be.podor.member.model;

import be.podor.member.dto.KakaoUserInfoDto;
import be.podor.member.dto.MemberInfoRequestDto;
import be.podor.member.dto.TwitterUserInfoDto;
import be.podor.security.jwt.refresh.RefreshToken;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberEnum memberRole;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

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

    public void updateMember(MemberInfoRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.profilePic = requestDto.getProfilePic();
    }

    public void setMemberRole(MemberEnum memberRole){
        this.memberRole = memberRole;
    }
}
