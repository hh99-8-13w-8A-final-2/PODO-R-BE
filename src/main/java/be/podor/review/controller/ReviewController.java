package be.podor.review.controller;

import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final SimpMessagingTemplate template;

    // 리뷰 작성
    @PostMapping("/api/musicals/{musicalId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable Long musicalId,
            @RequestBody ReviewRequestDto requestDto
    ) {
        Review review = reviewService.createReview(musicalId, requestDto);

        // 소켓에 쏘기? 소켓 연결이 없어서 안 될지도 나중에 클라이언트랑 붙여보기
        liveReview(review);

        return ResponseEntity.ok().build();
    }

    // 라이브 리뷰
    @MessageMapping("/reviews")
    public void liveReview(Review review) {
        ReviewLiveResponseDto response = ReviewLiveResponseDto.of(review);

        template.convertAndSend("/sub/reviews", response);
    }
}
