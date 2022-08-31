package be.podor.review.service;

import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.review.dto.ReviewRequestDto;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.repository.TheaterSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MusicalRepository musicalRepository;
    private final TheaterSeatRepository theaterSeatRepository;

    // 리뷰 작성
    public void createReview(Long musicalId, ReviewRequestDto requestDto) {
        Musical musical = musicalRepository.findById(musicalId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 뮤지컬입니다.")
        );

        TheaterSeat theaterSeat = theaterSeatRepository.findByFloorAndSectionAndSeatRowAndSeat(
                requestDto.getFloor(),
                requestDto.getSection(),
                requestDto.getRow(),
                requestDto.getSeat()
        ).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 좌석입니다.")
        );

        Review review = Review.of(theaterSeat, musical, requestDto);

        reviewRepository.save(review);
    }
}
