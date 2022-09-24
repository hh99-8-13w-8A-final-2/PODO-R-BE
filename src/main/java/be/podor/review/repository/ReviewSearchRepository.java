package be.podor.review.repository;

import be.podor.review.dto.search.SearchDto;
import be.podor.review.model.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static be.podor.review.model.QReview.review;
import static be.podor.review.model.tag.QReviewTag.reviewTag;
import static be.podor.theater.model.QTheaterSeat.theaterSeat;

@Repository
@RequiredArgsConstructor
public class ReviewSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Review> findReviewSearch(Long musicalId, SearchDto searchDto, Pageable pageable) {
        if (searchDto.getTags() != null && !searchDto.getTags().isEmpty()) {
            return findReviewSearchWithTags(musicalId, searchDto, pageable);
        }
        return findReviewSearchWithoutTags(musicalId, searchDto, pageable);
    }

    public Page<Review> findReviewSearchWithTags(Long musicalId, SearchDto searchDto, Pageable pageable) {
        List<Review> result = jpaQueryFactory.selectFrom(review)
                .innerJoin(review.theaterSeat, theaterSeat).fetchJoin()
                .leftJoin(review.reviewTags, reviewTag)
                .where(
                        searchBase(searchDto, musicalId),
                        reviewTagIn(searchDto.getTags())
                )
                .groupBy(review.reviewId)
                .having(reviewIdHaving(searchDto.getTags()))
                .orderBy(getOrderSpecifier(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> fetch = jpaQueryFactory.select(review.reviewId).from(review)
                .leftJoin(review.reviewTags, reviewTag)
                .where(
                        searchBase(searchDto, musicalId),
                        reviewTagIn(searchDto.getTags())
                )
                .groupBy(review.reviewId)
                .having(reviewIdHaving(searchDto.getTags()))
                .fetch();

        int total = fetch.size();

        return new PageImpl<>(result, pageable, total);
    }

    public Page<Review> findReviewSearchWithoutTags(Long musicalId, SearchDto searchDto, Pageable pageable) {
        List<Review> result = jpaQueryFactory.selectFrom(review)
                .innerJoin(review.theaterSeat, theaterSeat).fetchJoin()
                .where(
                        searchBase(searchDto, musicalId)
                )
                .orderBy(getOrderSpecifier(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(Wildcard.count).from(review)
                .where(
                        searchBase(searchDto, musicalId)
                )
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanBuilder searchBase(SearchDto searchDto, Long musicalId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(review.musical.musicalId.eq(musicalId));

        if (searchDto.getGrade() != null) {
            booleanBuilder.and(review.grade.eq(searchDto.getGrade()));
        }

        if (searchDto.getFloor() != null) {
            booleanBuilder.and(review.theaterSeat.floor.eq(searchDto.getFloor()));
        }

        if (searchDto.getSection() != null) {
            booleanBuilder.and(review.theaterSeat.section.eq(searchDto.getSection()));
        }

        if (searchDto.getRow() != null) {
            booleanBuilder.and(review.theaterSeat.seatRow.eq(searchDto.getRow()));
        }

        if (searchDto.getSeat() != null) {
            booleanBuilder.and(review.theaterSeat.seat.eq(searchDto.getSeat()));
        }

        if (searchDto.getGap() != null) {
            booleanBuilder.and(review.evaluation.gap.eq(searchDto.getGap()));
        }

        if (searchDto.getSight() != null) {
            booleanBuilder.and(review.evaluation.sight.eq(searchDto.getSight()));
        }

        if (searchDto.getSound() != null) {
            booleanBuilder.and(review.evaluation.sound.eq(searchDto.getSound()));
        }

        if (searchDto.getLight() != null) {
            booleanBuilder.and(review.evaluation.light.eq(searchDto.getLight()));
        }

        if (searchDto.getBlock() != null) {
            booleanBuilder.and(review.block.eq(searchDto.getBlock()));
        }

        if (searchDto.getOperaGlass() != null) {
            booleanBuilder.and(review.operaGlass.eq(searchDto.getOperaGlass()));
        }

        return booleanBuilder;
    }

    private BooleanExpression reviewTagIn(Set<String> tags) {
        if (tags != null) {
            return reviewTag.tag.tag.in(tags);
        }
        return null;
    }

    private BooleanExpression reviewIdHaving(Set<String> tags) {
        if (tags != null) {
            return Wildcard.count.eq((long) tags.size());
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        Sort.Order order = pageable.getSort().iterator().next();

        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        String property = order.getProperty();
        Path<Object> fieldPath;

        if (property.equals("score")) {
            fieldPath = Expressions.path(Object.class, review.score, property);
        } else if (property.equals("createdAt")) {
            fieldPath = Expressions.path(Object.class, review.createdAt, property);
        } else {
            fieldPath = Expressions.path(Object.class, review.createdAt, "createdAt");
        }

        return new OrderSpecifier(direction, fieldPath);
    }
}
