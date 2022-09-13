package be.podor.reviewheart.controller;

import be.podor.reviewheart.service.ReviewHeartService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewHeartController {

    private final ReviewHeartService reviewHeartService;

    // 생성
    @PostMapping("/api/hearts")
    public ResponseEntity<?> heartReview(
            @RequestParam("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewHeartService.heartReview(reviewId, userDetails);

        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/api/hearts")
    public ResponseEntity<?> unHeartReview(
            @RequestParam("reviewId") Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewHeartService.unHeartReview(reviewId, userDetails);

        return ResponseEntity.ok().build();
    }
}
