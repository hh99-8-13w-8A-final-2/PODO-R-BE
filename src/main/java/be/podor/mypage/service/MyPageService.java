package be.podor.mypage.service;

import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ReviewRepository reviewRepository;

    public List<ReviewListResponseDto> getMyReviews(Pageable pageable,
                                                    UserDetailsImpl userDetails) {

        List<Review> myReviewList = reviewRepository.findByCreatedByOrderByCreatedAtDesc(userDetails.getMemberId(), pageable);
        List<ReviewListResponseDto> reviewListResponseDtos = myReviewList.stream()
                .map(ReviewListResponseDto::of)
                .collect(Collectors.toList());
        return reviewListResponseDtos;
    }

}
