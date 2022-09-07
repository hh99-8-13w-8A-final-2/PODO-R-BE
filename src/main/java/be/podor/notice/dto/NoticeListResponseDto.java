package be.podor.notice.dto;

import be.podor.notice.model.Notice;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeListResponseDto {
    private Long noticeId;
    private String title;
    private LocalDateTime createdAt;

    public static NoticeListResponseDto of(Notice notice) {
        return NoticeListResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
