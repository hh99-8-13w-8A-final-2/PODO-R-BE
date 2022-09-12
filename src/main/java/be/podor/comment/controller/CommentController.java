package be.podor.comment.controller;

import be.podor.comment.dto.CommentRequestDto;
import be.podor.comment.dto.CommentResponseDto;
import be.podor.comment.service.CommentService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 리뷰에 달린 댓글들 조회
    @GetMapping("/api/comments")
    public ResponseEntity<?> getReviewComments(@RequestParam("reviewId") Long reviewId) {
        List<CommentResponseDto> responseDto = commentService.findReviewComments(reviewId);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 댓글 작성
    @PostMapping("/api/comments")
    public ResponseEntity<?> createReviewComment(
            @RequestParam("reviewId") Long reviewId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        CommentResponseDto responseDto = commentService.createReviewComment(reviewId, requestDto, userDetails);

        return ResponseEntity.ok(responseDto);
    }
}
