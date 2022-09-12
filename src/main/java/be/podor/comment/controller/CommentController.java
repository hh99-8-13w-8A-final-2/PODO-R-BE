package be.podor.comment.controller;

import be.podor.comment.dto.CommentResponseDto;
import be.podor.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
