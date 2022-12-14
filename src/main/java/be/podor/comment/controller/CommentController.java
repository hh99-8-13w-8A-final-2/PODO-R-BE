package be.podor.comment.controller;

import be.podor.comment.dto.CommentRequestDto;
import be.podor.comment.dto.CommentResponseDto;
import be.podor.comment.service.CommentService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 리뷰에 달린 댓글들 조회
    @GetMapping("/api/comments")
    public ResponseEntity<?> getReviewComments(
            @RequestParam("reviewId") Long reviewId,
            @PageableDefault(size = 20, page = 1) Pageable pageable
    ) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        Page<CommentResponseDto> responseDto = commentService.findReviewComments(reviewId, pageRequest);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 댓글 작성
    @PostMapping("/api/comments")
    public ResponseEntity<?> createReviewComment(
            @RequestParam("reviewId") Long reviewId,
            @Valid @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto responseDto = commentService.createReviewComment(reviewId, requestDto, userDetails);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 댓글 수정
    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<?> UpdateReviewComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto responseDto = commentService.updateReviewComment(commentId, requestDto, userDetails);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 댓글 수정
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<?> UpdateReviewComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.deleteReviewComment(commentId, userDetails);

        return ResponseEntity.ok().build();
    }
}
