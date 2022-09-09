package be.podor.review.controller;

import be.podor.review.dto.ReviewDetailResponseDto;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Review review = reviewService.createReview(musicalId, requestDto);

        return ResponseEntity.ok().build();
    }

    // 최근 리뷰 가져오기 for live
    @GetMapping("/api/reviews/live")
    public ResponseEntity<?> getRecentReviews() {
        // 최근 10개
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<ReviewLiveResponseDto> responseDtos = reviewService.getRecentReviews(pageRequest);

        return ResponseEntity.ok(responseDtos);
    }

    // 뮤지컬 선택시 해당 뮤지컬의 전체 리뷰 리스트 조회
    @GetMapping("/api/musicals/{musicalId}/reviews")
    public ResponseEntity<?> getMusicalReviews(
            @PathVariable Long musicalId,
            @PageableDefault(size = 20, page = 1) Pageable pageable
    ) {
        // 페이지번호 -1 처리, 1 2 3 4 -> 0, 1, 2, 3
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        Page<ReviewListResponseDto> responseDtos = reviewService.getMusicalReviews(musicalId, pageRequest);

        return ResponseEntity.ok(responseDtos);
    }

    // 리뷰 상세 조회
    @GetMapping("/api/musicals/{musicalId}/reviews/{reviewId}")
    public ResponseEntity<?> getMusicalReview(
            @PathVariable Long musicalId,
            @PathVariable Long reviewId
    ) {
        ReviewDetailResponseDto responseDto = reviewService.getReviewDetail(musicalId, reviewId);

        return ResponseEntity.ok(responseDto);
    }
}
