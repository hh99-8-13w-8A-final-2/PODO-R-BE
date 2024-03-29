package be.podor.review.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.member.service.MemberSearchService;
import be.podor.member.validator.MemberValidator;
import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.musical.validator.MusicalValidator;
import be.podor.review.dto.ReviewDetailResponseDto;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.dto.search.SearchDto;
import be.podor.review.model.Review;
import be.podor.review.model.reviewfile.ReviewFile;
import be.podor.review.model.tag.ReviewTag;
import be.podor.review.repository.ReviewRepository;
import be.podor.review.repository.ReviewSearchRepository;
import be.podor.review.repository.ReviewTagRepository;
import be.podor.review.validator.ReviewValidator;
import be.podor.reviewheart.repository.ReviewHeartRepository;
import be.podor.security.UserDetailsImpl;
import be.podor.tag.model.Tag;
import be.podor.tag.repository.TagRepository;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterSeatRepository;
import be.podor.theater.validator.TheaterSeatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewSearchRepository reviewSearchRepository;

    private final ReviewRepository reviewRepository;

    private final MusicalRepository musicalRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final TagRepository tagRepository;

    private final MemberRepository memberRepository;

    private final ReviewTagRepository reviewTagRepository;

    private final ReviewHeartRepository reviewHeartRepository;

    private final MemberSearchService memberSearchService;

    // 리뷰 작성
    @Transactional
    public Review createReview(Long musicalId, ReviewRequestDto requestDto) {
        Musical musical = MusicalValidator.validate(musicalRepository, musicalId);

        TheaterSeat theaterSeat = TheaterSeatValidator.validate(theaterSeatRepository, requestDto, musical);

        Set<Tag> tags = findExistTagsOrElseCreate(requestDto);

        Review review = Review.of(theaterSeat, musical, requestDto);

        reviewRepository.save(review);

        List<ReviewFile> reviewFiles = requestDto.getImgUrls().stream()
                .map(path -> ReviewFile.of(path, review))
                .collect(Collectors.toList());

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.of(review, tag))
                .collect(Collectors.toList());

        tags.forEach(tag -> tag.addReviewTags(reviewTags));

        review.addFiles(reviewFiles);
        review.addTags(reviewTags);

        return review;
    }

    // 최근 리뷰 가져오기 for live
    public List<ReviewLiveResponseDto> getRecentReviews(PageRequest pageRequest) {
        List<Review> reviews = reviewRepository.findTop10ByOrderByCreatedAtDesc();

        return reviews.stream()
                .map(ReviewLiveResponseDto::of)
                .collect(Collectors.toList());
    }

    // 뮤지컬 선택시 해당 뮤지컬의 전체 리뷰 리스트 조회
    public Page<ReviewListResponseDto> getMusicalReviews(
            Long musicalId, SearchDto searchDto, Pageable pageable, UserDetailsImpl userDetails
    ) {
        Page<Review> reviews = reviewSearchRepository.findReviewSearch(musicalId, searchDto, pageable);

        List<ReviewListResponseDto> reviewListResponseDtos = reviews.stream()
                .map(review -> {
                    Boolean heartChecked = userDetails != null && reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId());
                    return ReviewListResponseDto.of(review, heartChecked);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(reviewListResponseDtos, reviews.getPageable(), reviews.getTotalElements());
    }

    // 리뷰 상세 조회
    public ReviewDetailResponseDto getReviewDetail(Long musicalId, Long reviewId, UserDetailsImpl userDetails) {
        Review review = ReviewValidator.validate(reviewRepository, reviewId);

        Member member = MemberValidator.validate(memberRepository, review.getCreatedBy());

        Boolean heartChecked = userDetails != null && reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId());

        return ReviewDetailResponseDto.of(review, MemberDto.of(member), heartChecked);
    }

    // 리뷰 수정
    @Transactional
    public ReviewDetailResponseDto updateReview(Long musicalId, Long reviewId, ReviewRequestDto requestDto, UserDetailsImpl userDetails) {
        Review review = ReviewValidator.validate(reviewRepository, reviewId);

        if (!review.getCreatedBy().equals(userDetails.getMemberId())) {
            throw new IllegalArgumentException("다른 사용자의 리뷰를 수정할 수 없습니다.");
        }

        Musical musical = MusicalValidator.validate(musicalRepository, musicalId);

        TheaterSeat theaterSeat = TheaterSeatValidator.validate(theaterSeatRepository, requestDto, musical);

        Set<Tag> tags = findExistTagsOrElseCreate(requestDto);

        review.update(theaterSeat, musical, requestDto);

        List<ReviewFile> reviewFiles = requestDto.getImgUrls().stream()
                .map(path -> ReviewFile.of(path, review))
                .collect(Collectors.toList());

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.of(review, tag))
                .collect(Collectors.toList());

        review.addFiles(reviewFiles);

        // OneToMany ManyToOne 관계 정리
        List<ReviewTag> prevTags = new ArrayList<>(review.getReviewTags());
        review.addTags(reviewTags);
        reviewTagRepository.deleteAllInBatch(prevTags);

        Member member = MemberValidator.validate(memberRepository, review.getCreatedBy());

        Boolean heartChecked = reviewHeartRepository.existsByReviewAndCreatedBy(review, userDetails.getMemberId());

        return ReviewDetailResponseDto.of(review, MemberDto.of(member), heartChecked);
    }

    // 태그 가져오기
    public Set<Tag> findExistTagsOrElseCreate(ReviewRequestDto requestDto) {
        List<String> splitTags = Arrays.asList(requestDto.getTags().split(",\\s*"));

        Set<Tag> existTags = tagRepository.findByTagIn(splitTags);

        Set<String> existTagNames = existTags.stream()
                .map(Tag::getTag)
                .collect(Collectors.toSet());

        for (String splitTag : splitTags) {
            splitTag = splitTag.trim();

            if (splitTag.isEmpty()) {
                continue;
            }

            if (!existTagNames.contains(splitTag)) {
                existTags.add(tagRepository.save(new Tag(splitTag)));
            }
        }

        return existTags;
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, UserDetailsImpl userDetails) {
        reviewRepository.deleteByReviewIdAndCreatedBy(reviewId, userDetails.getMemberId());
    }
}
