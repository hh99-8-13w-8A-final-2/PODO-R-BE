package be.podor.member.model;

import be.podor.member.dto.responsedto.KakaoUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private Long twitterId;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column
    private String profilePic;

    @Column
    private LocalDateTime createdAt;


    public static Member of(KakaoUserInfoDto kakaoUserInfoDto) {

        return Member.builder()
                .nickname(kakaoUserInfoDto.getNickname())
                .profilePic(kakaoUserInfoDto.getProfilePic())
                .createdAt(LocalDateTime.now())
                .kakaoId(kakaoUserInfoDto.getKakaoId())
                .build();
    }
}
