package be.podor.member.model;

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


}
