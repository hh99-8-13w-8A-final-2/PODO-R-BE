package be.podor.review.repository;

import be.podor.review.dto.SearchDto;
import be.podor.review.model.Review;
import be.podor.review.model.reviewInfo.ScoreEnum;
import com.querydsl.core.types.Expression;
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
        if (searchDto.getTags() != null) {
            return findReviewSearchWithTags(musicalId, searchDto, pageable);
        }
        return findReviewSearchWithoutTags(musicalId, searchDto, pageable);
    }

    public Page<Review> findReviewSearchWithTags(Long musicalId, SearchDto searchDto, Pageable pageable) {
        List<Review> result = jpaQueryFactory.selectFrom(review)
                .innerJoin(review.theaterSeat, theaterSeat).fetchJoin()
                .leftJoin(review.reviewTags, reviewTag)
                .where(
                        review.musical.musicalId.eq(musicalId),
                        gapEq(searchDto.getGap()),
                        sightEq(searchDto.getSight()),
                        soundEq(searchDto.getSound()),
                        lightEq(searchDto.getLight()),
                        blockEq(searchDto.getBlock()),
                        operaGlassEq(searchDto.getOperaGlass()),
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
                        review.musical.musicalId.eq(musicalId),
                        gapEq(searchDto.getGap()),
                        sightEq(searchDto.getSight()),
                        soundEq(searchDto.getSound()),
                        lightEq(searchDto.getLight()),
                        blockEq(searchDto.getBlock()),
                        operaGlassEq(searchDto.getOperaGlass()),
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
                        review.musical.musicalId.eq(musicalId),
                        gapEq(searchDto.getGap()),
                        sightEq(searchDto.getSight()),
                        soundEq(searchDto.getSound()),
                        lightEq(searchDto.getLight()),
                        blockEq(searchDto.getBlock()),
                        operaGlassEq(searchDto.getOperaGlass())
                )
                .orderBy(getOrderSpecifier(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(Wildcard.count).from(review)
                .where(
                        review.musical.musicalId.eq(musicalId),
                        gapEq(searchDto.getGap()),
                        sightEq(searchDto.getSight()),
                        soundEq(searchDto.getSound()),
                        lightEq(searchDto.getLight()),
                        blockEq(searchDto.getBlock()),
                        operaGlassEq(searchDto.getOperaGlass())
                )
                .fetchFirst();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression gapEq(ScoreEnum gap) {
        if (gap != null) {
            return review.evaluation.gap.eq(gap);
        }
        return null;
    }

    private BooleanExpression sightEq(ScoreEnum sight) {
        if (sight != null) {
            return review.evaluation.sight.eq(sight);
        }
        return null;
    }

    private BooleanExpression soundEq(ScoreEnum sound) {
        if (sound != null) {
            return review.evaluation.sound.eq(sound);
        }
        return null;
    }

    private BooleanExpression lightEq(ScoreEnum light) {
        if (light != null) {
            return review.evaluation.light.eq(light);
        }
        return null;
    }

    private BooleanExpression blockEq(Boolean block) {
        if (block != null) {
            return review.block.eq(block);
        }
        return null;
    }

    private BooleanExpression operaGlassEq(Boolean operaGlass) {
        if (operaGlass != null) {
            return review.operaGlass.eq(operaGlass);
        }
        return null;
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
