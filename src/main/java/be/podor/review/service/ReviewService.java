package be.podor.review.service;

import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.review.dto.ReviewLiveResponseDto;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MusicalRepository musicalRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    // 리뷰 작성
    public Review createReview(Long musicalId, ReviewRequestDto requestDto) {
        Musical musical = musicalRepository.findById(musicalId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 뮤지컬입니다.")
        );

        TheaterSeat theaterSeat = theaterSeatRepository.findByFloorAndSectionAndSeatRowAndSeatAndTheater_TheaterId(
                requestDto.getFloor(),
                requestDto.getSection(),
                requestDto.getRow(),
                requestDto.getSeat(),
                musical.getTheater().getTheaterId()
        ).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 좌석입니다.")
        );

        Review review = Review.of(theaterSeat, musical, requestDto);

        return reviewRepository.save(review);
    }

    // 최근 리뷰 가져오기 for live
    public List<ReviewLiveResponseDto> getRecentReviews(PageRequest pageRequest) {
        Page<Review> reviews = reviewRepository.findAll(pageRequest);

        return reviews.stream()
                .map(ReviewLiveResponseDto::of)
                .collect(Collectors.toList());
    }
}
