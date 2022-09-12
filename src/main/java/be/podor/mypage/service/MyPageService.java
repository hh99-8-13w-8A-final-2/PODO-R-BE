package be.podor.mypage.service;

import be.podor.musical.dto.MusicalListResponseDto;
import be.podor.musical.model.Musical;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ReviewRepository reviewRepository;

    public Page<ReviewListResponseDto> getMyReviews(Pageable pageable,
                                                    UserDetailsImpl userDetails) {

        Page<Review> myReviewList = reviewRepository.findByCreatedByOrderByCreatedAtDesc(userDetails.getMemberId(), pageable);
        List<ReviewListResponseDto> reviewListResponseDtos = myReviewList.stream()
                .map(ReviewListResponseDto::of)
                .collect(Collectors.toList());
        return new PageImpl<>(reviewListResponseDtos, myReviewList.getPageable(), myReviewList.getTotalElements());
    }

    public Page<MusicalListResponseDto> getMyMusicals(Long memberId,
                                                      Pageable pageable) {
        Page<Musical> myMusicalList = reviewRepository.findByReviewIdGroupByMusical(memberId, pageable);
        List<MusicalListResponseDto> musicalListResponseDtos = myMusicalList.stream()
                .map(MusicalListResponseDto::of)
                .collect(Collectors.toList());
        return new PageImpl<>(musicalListResponseDtos, myMusicalList.getPageable(), myMusicalList.getTotalElements());
    }
}
