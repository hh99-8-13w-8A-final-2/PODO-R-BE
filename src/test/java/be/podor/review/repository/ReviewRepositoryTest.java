package be.podor.review.repository;

import be.podor.config.AuditingConfig;
import be.podor.member.model.Member;
import be.podor.member.model.MemberEnum;
import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.Evaluation;
import be.podor.review.model.reviewInfo.ScoreEnum;
import be.podor.security.UserDetailsImpl;
import be.podor.theater.model.Theater;
import be.podor.theater.model.TheaterSeat;
import be.podor.theater.model.type.FloorType;
import be.podor.theater.model.type.GradeType;
import be.podor.theater.repository.TheaterRepository;
import be.podor.theater.repository.TheaterSeatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@TestPropertySource(properties = {"spring.jpa.properties.hibernate.format_sql=true"})
@Import(AuditingConfig.class)
@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MusicalRepository musicalRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private TheaterSeatRepository theaterSeatRepository;

    private Member member;

    private Musical musical;

    private Theater theater;

    private TheaterSeat theaterSeat;

    @PostConstruct
    void setLoginUserAndData() {
        member = Member.builder()
                .id(1L)
                .nickname("테스트유저")
                .profilePic("테스트사진")
                .memberRole(MemberEnum.ROLE_MEMBER)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(member.getId(), member.getMemberRole().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        theater = theaterRepository.save(Theater.builder()
                .theaterId(1L)
                .theaterName("테스트극장")
                .theaterAddr("테스트주소")
                .la(0.0)
                .lo(0.0)
                .theaterSeatImage("테스트이미지")
                .build());

        musical = musicalRepository.save(Musical.builder()
                .musicalId(1L)
                .musicalName("테스트뮤지컬")
                .musicalPoster("테스트포스터")
                .openDate(LocalDate.of(2022, 10, 5))
                .closeDate(LocalDate.of(2022, 10, 7))
                .theater(theater)
                .build());

        theaterSeat = theaterSeatRepository.save(TheaterSeat.builder()
                .seatId(1L)
                .floor(FloorType.FIRST)
                .seat(1)
                .theater(theater)
                .build());
    }

    @BeforeEach
    void setUp() {
        List<Review> reviewList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Evaluation evaluation = Evaluation.builder()
                    .gap(ScoreEnum.GOOD)
                    .light(ScoreEnum.GOOD)
                    .sight(ScoreEnum.GOOD)
                    .sound(ScoreEnum.GOOD)
                    .build();

            reviewList.add(Review.builder()
                    .evaluation(evaluation)
                    .content("test_content_" + i)
                    .grade(GradeType.VIP)
                    .score(1.0)
                    .musical(musical)
                    .theaterSeat(theaterSeat)
                    .block(false)
                    .operaGlass(false)
                    .build());
        }

        reviewRepository.saveAll(reviewList);
    }

    @DisplayName("MemberId Auditing Test")
    @Test
    void auditing() {
        int size = 10;
        Page<Review> reviewPage = reviewRepository.findByCreatedByOrderByCreatedAtDesc(member.getId(), PageRequest.of(0, size));

        Assertions.assertThat(reviewPage.getSize()).isEqualTo(size);
        Assertions.assertThat(reviewPage.getContent().get(0).getCreatedBy()).isEqualTo(member.getId());
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
    }
}