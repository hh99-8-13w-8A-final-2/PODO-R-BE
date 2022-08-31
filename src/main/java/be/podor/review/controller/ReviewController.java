package be.podor.review.controller;

import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/api/musicals/{musicalId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long musicalId,
            @RequestBody ReviewRequestDto requestDto
    ) {
        reviewService.createReview(musicalId, requestDto);

        return ResponseEntity.ok().build();
    }
}
