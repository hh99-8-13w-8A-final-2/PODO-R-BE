package be.podor.mypage.controller;


import be.podor.musical.dto.MusicalListResponseDto;
import be.podor.mypage.dto.MyPageRequestDto;
import be.podor.mypage.service.MyPageService;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService mypageService;

    //내가 쓴 게시글 (Post)
    @GetMapping("/mypage/reviews")
    public ResponseEntity<?> getMyReview(@PageableDefault(size = 20, page = 1) Pageable pageable,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PageRequest request = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<ReviewListResponseDto> responseDto = mypageService.getMyReviews(request, userDetails);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/mypage/musicals")
    public ResponseEntity<?> getMyMusical(@PageableDefault(size = 20, page = 1) Pageable pageable,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PageRequest request = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<MusicalListResponseDto> responseDto = mypageService.getMyMusicals(userDetails, request);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/mypage/{musicalId}/reviews")
    public ResponseEntity<?> getMyMusicalReviews(@PageableDefault(size = 20, page = 1) Pageable pageable,
                                                 @PathVariable Long musicalId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PageRequest request = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<ReviewListResponseDto> responseDto = mypageService.getMyMusicalReviews(request, musicalId, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/mypage/update")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @Valid @RequestBody MyPageRequestDto requestDto) {
        mypageService.updateMemberInfo(userDetails, requestDto);
        return ResponseEntity.ok().build();
    }
}
