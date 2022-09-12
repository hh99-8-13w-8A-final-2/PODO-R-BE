package be.podor.mypage.service;

import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ReviewRepository reviewRepository;

    public ResponseEntity<?> getMyReviews(UserDetailsImpl userDetails) {

        List<Review> myReviewList = reviewRepository.findByCreatedByOrderByCreatedAtDesc(userDetails.getMemberId());
        List<ReviewListResponseDto> reviewListResponseDtos = myReviewList.stream()
                .map(ReviewListResponseDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(reviewListResponseDtos);
    }

}
