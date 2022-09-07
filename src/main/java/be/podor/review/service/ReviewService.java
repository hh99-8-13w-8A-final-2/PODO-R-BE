package be.podor.review.service;

import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.musical.validator.MusicalValidator;
import be.podor.review.dto.ReviewDetailResponseDto;
import be.podor.review.dto.ReviewListResponseDto;
import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.model.reviewfile.ReviewFile;
import be.podor.review.model.tag.ReviewTag;
import be.podor.review.model.tag.Tag;
import be.podor.review.repository.ReviewRepository;
import be.podor.review.repository.TagRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final MusicalRepository musicalRepository;

    private final TheaterSeatRepository theaterSeatRepository;

    private final TagRepository tagRepository;

    // 리뷰 작성
    @Transactional
    public Review createReview(Long musicalId, ReviewRequestDto requestDto) {
        Musical musical = MusicalValidator.validate(musicalRepository, musicalId);

        TheaterSeat theaterSeat = TheaterSeatValidator.validate(theaterSeatRepository, requestDto, musical);

        String[] convertTags = requestDto.getTags().split(", ");

        List<Tag> tags = Arrays.stream(convertTags)
                .map(Tag::new)
                .collect(Collectors.toList());

        tags = tagRepository.saveAll(tags);

        Review review = Review.of(theaterSeat, musical, requestDto);

        List<ReviewFile> reviewFiles = requestDto.getImgUrls().stream()
                .map(path -> ReviewFile.of(path, review))
                .collect(Collectors.toList());

        List<ReviewTag> reviewTags = tags.stream()
                .map(tag -> ReviewTag.of(review, tag))
                .collect(Collectors.toList());

        review.addFiles(reviewFiles);
        review.addTags(reviewTags);

        return reviewRepository.save(review);
    }

    // 최근 리뷰 가져오기 for live
    public List<ReviewLiveResponseDto> getRecentReviews(PageRequest pageRequest) {
        List<Review> reviews = reviewRepository.findAll(pageRequest).toList();

        return reviews.stream()
                .map(ReviewLiveResponseDto::of)
                .collect(Collectors.toList());
    }

    // 뮤지컬 선택시 해당 뮤지컬의 전체 리뷰 리스트 조회
    public Page<ReviewListResponseDto> getMusicalReviews(Long musicalId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByMusical_MusicalIdOrderByCreatedAtDesc(musicalId, pageable);

        List<ReviewListResponseDto> reviewListResponseDtos = reviews.stream()
                .map(ReviewListResponseDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewListResponseDtos, reviews.getPageable(), reviews.getTotalElements());
    }

    // 리뷰 상세 조회
    public ReviewDetailResponseDto getReviewDetail(Long musicalId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다.")
        );

        return ReviewDetailResponseDto.of(review);
    }
}
