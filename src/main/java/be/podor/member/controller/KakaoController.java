package be.podor.member.controller;


import be.podor.member.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;


    @GetMapping("/kakao/callback")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        kakaoService.kakaoLogin(code, response);
    }
    
}



