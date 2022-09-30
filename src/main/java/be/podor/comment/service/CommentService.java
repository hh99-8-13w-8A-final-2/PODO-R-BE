package be.podor.comment.service;

import be.podor.comment.dto.CommentRequestDto;
import be.podor.comment.dto.CommentResponseDto;
import be.podor.comment.model.Comment;
import be.podor.comment.repository.CommentRepository;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.member.validator.MemberValidator;
import be.podor.review.model.Review;
import be.podor.review.repository.ReviewRepository;
import be.podor.review.validator.ReviewValidator;
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
public class CommentService {

    private final CommentRepository commentRepository;

    private final ReviewRepository reviewRepository;

    private final MemberRepository memberRepository;

    // 리뷰에 달린 댓글들 조회
    public Page<CommentResponseDto> findReviewComments(Long reviewId, Pageable pageable) {
        Page<Object[]> queryResult = commentRepository.findCommentsByReviewId(reviewId, pageable);

        List<CommentResponseDto> responseDto = queryResult.stream()
                .map(obj -> CommentResponseDto.of((Member) obj[0], (Comment) obj[1]))
                .collect(Collectors.toList());

        return new PageImpl<>(responseDto, queryResult.getPageable(), queryResult.getTotalElements());
    }

    // 리뷰 댓글 작성
    public CommentResponseDto createReviewComment(Long reviewId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Review review = ReviewValidator.validate(reviewRepository, reviewId);
        Member member = MemberValidator.validate(memberRepository, userDetails.getMemberId());

        Comment comment = commentRepository.save(Comment.of(requestDto, review));

        return CommentResponseDto.of(member, comment);
    }

    // 리뷰 댓글 수정
    @Transactional
    public CommentResponseDto updateReviewComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException(commentId + "번에 해당하는 댓글이 존재하지 않습니다.")
        );

        comment.update(requestDto);
        Member member = MemberValidator.validate(memberRepository, userDetails.getMemberId());

        return CommentResponseDto.of(member, comment);
    }

    // 리뷰 댓글 삭제
    @Transactional
    public void deleteReviewComment(Long commentId, UserDetailsImpl userDetails) {
        commentRepository.deleteByCommentIdAndCreatedBy(commentId, userDetails.getMemberId());
    }
}
