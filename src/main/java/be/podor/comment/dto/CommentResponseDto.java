package be.podor.comment.dto;

import be.podor.comment.model.Comment;
import be.podor.member.model.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {
    private Long memberId;
    private String nickname;
    private String profilePic;

    private Long commentId;
    private String content;

    private LocalDateTime createdAt;

    public static CommentResponseDto of(CommentQueryDto commentQueryDto) {
        Member member = commentQueryDto.getMember();
        Comment comment = commentQueryDto.getComment();

        return CommentResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profilePic(member.getProfilePic())
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
