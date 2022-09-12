package be.podor.comment.dto;

import be.podor.comment.model.Comment;
import be.podor.member.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentQueryDto {
    Member member;
    Comment comment;
}
