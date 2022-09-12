package be.podor.comment.service;

import be.podor.comment.dto.CommentQueryDto;
import be.podor.comment.dto.CommentResponseDto;
import be.podor.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 리뷰에 달린 댓글들 조회
    public List<CommentResponseDto> findReviewComments(Long reviewId) {
        List<CommentQueryDto> queryResult = commentRepository.findCommentsByReviewId(reviewId);

        return queryResult.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }
}
