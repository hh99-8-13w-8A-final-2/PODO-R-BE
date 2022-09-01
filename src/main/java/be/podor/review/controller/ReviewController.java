package be.podor.review.controller;

import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        // 소켓에 쏘기
        liveReview(review);

        return ResponseEntity.ok().build();
    }

    // // 최근 리뷰 가져오기 for live
    @GetMapping("/api/reviews/live")
    public ResponseEntity<?> getRecentReviews() {
        // 최근 10개
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<ReviewLiveResponseDto> responseDtos = reviewService.getRecentReviews(pageRequest);

        return ResponseEntity.ok(responseDtos);
    }

    // 라이브 리뷰
    @MessageMapping("/reviews")
    public void liveReview(Review review) {
        ReviewLiveResponseDto response = ReviewLiveResponseDto.of(review);

        template.convertAndSend("/sub/reviews", response);
    }
}
