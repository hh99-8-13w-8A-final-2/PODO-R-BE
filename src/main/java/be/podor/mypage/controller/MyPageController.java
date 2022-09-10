package be.podor.mypage.controller;


import be.podor.mypage.service.MyPageService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService mypageService;

    //내가 쓴 게시글 (Post)
    @GetMapping("/mypage/reviews")
    public ResponseEntity<?> getMyBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyReviews(userDetails);
    }

}
