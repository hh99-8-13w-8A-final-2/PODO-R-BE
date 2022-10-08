package be.podor.review.repository;

import be.podor.config.AuditingConfig;
import be.podor.member.model.Member;
import be.podor.member.model.MemberEnum;
import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.Evaluation;
import be.podor.review.model.reviewInfo.ScoreEnum;
import be.podor.review.model.tag.ReviewTag;
import be.podor.security.UserDetailsImpl;
import be.podor.tag.model.Tag;
import be.podor.tag.repository.TagRepository;
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@TestPropertySource(properties = {"spring.jpa.properties.hibernate.format_sql=true"})
@Import(AuditingConfig.class)
@DataJpaTest
public class ReviewRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MusicalRepository musicalRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private TheaterSeatRepository theaterSeatRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ReviewTagRepository reviewTagRepository;

    private Member member;

    private Musical musical;

    private Theater theater;

    private TheaterSeat theaterSeat;

    private List<Tag> tags;

    @BeforeEach
    void setUp() {
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

        tags = tagRepository.saveAll(
                Arrays.asList(
                        new Tag("테스트태그0"),
                        new Tag("테스트태그1"),
                        new Tag("테스트태그2"),
                        new Tag("테스트태그3"),
                        new Tag("테스트태그4"),
                        new Tag("테스트태그5"),
                        new Tag("테스트태그6"),
                        new Tag("테스트태그7"),
                        new Tag("테스트태그8"),
                        new Tag("테스트태그9")
                ));

        List<Review> reviewList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Evaluation evaluation = Evaluation.builder()
                    .gap(ScoreEnum.GOOD)
                    .light(ScoreEnum.GOOD)
                    .sight(ScoreEnum.GOOD)
                    .sound(ScoreEnum.GOOD)
                    .build();

            Review review = Review.builder()
                    .evaluation(evaluation)
                    .content("test_content_" + i)
                    .grade(GradeType.VIP)
                    .score(1.0)
                    .musical(musical)
                    .theaterSeat(theaterSeat)
                    .block(false)
                    .operaGlass(false)
                    .build();

            List<ReviewTag> reviewTags = tags.stream()
                    .map(tag -> ReviewTag.of(review, tag))
                    .collect(Collectors.toList());

            review.addTags(reviewTags);

            reviewList.add(review);
        }

        reviewRepository.saveAll(reviewList);

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("MemberId Auditing Test")
    @Test
    void auditing() {
        int size = 10;
        Page<Review> reviewPage = reviewRepository.findByCreatedByOrderByCreatedAtDesc(member.getId(), PageRequest.of(0, size));

        Assertions.assertThat(reviewPage.getSize()).isEqualTo(size);
        Assertions.assertThat(reviewPage.getContent().get(0).getCreatedBy()).isEqualTo(member.getId());
    }

    @DisplayName("N + 1 Test")
    @Test
    void nPlusOne() {
        int size = 10;
        Review review = reviewRepository.findAll().get(0);
        Set<String> tagNames = tags.stream().map(Tag::getTag).collect(Collectors.toSet());

        for (ReviewTag reviewTag : review.getReviewTags()) {
            Assertions.assertThat(reviewTag.getTag().getTag()).isIn(tagNames);
        }
    }

    @AfterEach
    void tearDown() {
        theaterRepository.deleteAll();
    }
}