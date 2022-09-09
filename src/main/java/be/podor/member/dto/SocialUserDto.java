package be.podor.member.dto;


import be.podor.security.jwt.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserDto {
    private MemberDto memberDto;
    private TokenDto tokenDto;

}
