package be.podor.notice.dto;

import be.podor.member.dto.MemberDto;
import be.podor.notice.model.Notice;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponseDto {
    private MemberDto member;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static NoticeResponseDto of(Notice notice, MemberDto member) {
        return NoticeResponseDto.builder()
                .member(member)
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
