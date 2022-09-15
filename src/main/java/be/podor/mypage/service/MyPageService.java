package be.podor.mypage.service;

import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.musical.dto.MusicalListResponseDto;
import be.podor.musical.model.Musical;
import be.podor.mypage.dto.MyPageRequestDto;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.reviewheart.repository.ReviewHeartRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    private final ReviewHeartRepository reviewHeartRepository;

    public Page<ReviewListResponseDto> getMyReviews(Pageable pageable,
                                                    UserDetailsImpl userDetails) {

        Page<Review> myReviewList = reviewRepository.findByCreatedByOrderByCreatedAtDesc(userDetails.getMemberId(), pageable);
        List<ReviewListResponseDto> reviewListResponseDtos = myReviewList.stream()
                .map(review -> {
                    Boolean heartChecked = userDetails != null && reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId());
                    return ReviewListResponseDto.of(review, heartChecked);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(reviewListResponseDtos, myReviewList.getPageable(), myReviewList.getTotalElements());
    }

    public Page<MusicalListResponseDto> getMyMusicals(UserDetailsImpl userDetails,
                                                      Pageable pageable) {
        Page<Musical> myMusicalList = reviewRepository.findByReviewIdGroupByMusical(userDetails.getMemberId(), pageable);
        List<MusicalListResponseDto> musicalListResponseDtos = myMusicalList.stream()
                .map(MusicalListResponseDto::of)
                .collect(Collectors.toList());
        return new PageImpl<>(musicalListResponseDtos, myMusicalList.getPageable(), myMusicalList.getTotalElements());
    }

    public Page<ReviewListResponseDto> getMyMusicalReviews(Pageable pageable,
                                                           Long musicalId,
                                                           UserDetailsImpl userDetails) {
        Page<Review> myMusicalReviewList = reviewRepository.findByMusical_MusicalIdAndCreatedBy(musicalId, userDetails.getMemberId(), pageable);
        List<ReviewListResponseDto> reviewListResponseDtos = myMusicalReviewList.stream()
                .map(review -> {
                    Boolean heartChecked = reviewHeartRepository.existsByReviewAndCreatedBy(review, memberId);
                    return ReviewListResponseDto.of(review, heartChecked);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(reviewListResponseDtos, myMusicalReviewList.getPageable(), myMusicalReviewList.getTotalElements());
    }

    @Transactional
    public void updateMemberInfo(UserDetailsImpl userDetails, MyPageRequestDto requestDto) {
        Member member = memberRepository.findById(userDetails.getMemberId()).orElseThrow(
                () -> new IllegalArgumentException("아이디가 없습니다."));
        member.updateMember(requestDto);
    }
}
