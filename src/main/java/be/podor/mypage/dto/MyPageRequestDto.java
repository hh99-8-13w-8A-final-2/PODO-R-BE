package be.podor.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageRequestDto {
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "[ㄱ-ㅎ가-힣a-zA-Z\\d]{1,20}$")
    private String nickname;
    private String profilePic;
}
