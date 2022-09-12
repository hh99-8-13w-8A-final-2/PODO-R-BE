package be.podor.mypage.controller;


import be.podor.musical.model.Musical;
import be.podor.mypage.dto.MyPageRequestDto;
import be.podor.mypage.service.MyPageService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService mypageService;

    //내가 쓴 게시글 (Post)
    @GetMapping("/mypage/reviews")
    public ResponseEntity<?> getMyReview(@PageableDefault(size = 20, page = 1) Pageable pageable,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(mypageService.getMyReviews(pageable,userDetails));
    }

}
