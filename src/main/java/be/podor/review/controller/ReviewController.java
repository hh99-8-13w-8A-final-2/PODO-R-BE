package be.podor.review.controller;

import be.podor.review.dto.*;
import be.podor.review.dto.search.SearchDto;
import be.podor.review.dto.search.SearchRequestParam;
import be.podor.review.model.Review;
import be.podor.review.service.ReviewService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private final SearchRequestParam SEARCH_NONE = new SearchRequestParam();

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
            SearchRequestParam searchRequestParam,
            @PathVariable Long musicalId,
            @PageableDefault(size = 20, page = 1, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 페이지번호 -1 처리, 1 2 3 4 -> 0, 1, 2, 3
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        // 검색 객체 처리
        if (searchRequestParam == null) {
            searchRequestParam = SEARCH_NONE;
        }

        Page<ReviewListResponseDto> responseDto = reviewService.getMusicalReviews(
                musicalId,
                SearchDto.of(searchRequestParam),
                pageRequest,
                userDetails
        );

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 상세 조회
    @GetMapping("/api/musicals/{musicalId}/reviews/{reviewId}")
    public ResponseEntity<?> getMusicalReview(
            @PathVariable Long musicalId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewDetailResponseDto responseDto = reviewService.getReviewDetail(musicalId, reviewId, userDetails);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 수정
    @PutMapping("/api/musicals/{musicalId}/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long musicalId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ReviewDetailResponseDto responseDto = reviewService.updateReview(musicalId, reviewId, requestDto, userDetails);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 삭제
    @DeleteMapping("/api/musicals/{musicalId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long musicalId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        reviewService.deleteReview(reviewId, userDetails);

        return ResponseEntity.ok().build();
    }
}
