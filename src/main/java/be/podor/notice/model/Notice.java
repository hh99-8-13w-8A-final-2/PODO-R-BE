package be.podor.notice.model;

import be.podor.notice.dto.NoticeRequestDto;
import be.podor.share.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public static Notice of(NoticeRequestDto requestDto) {
        return Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();
    }

    public void update(NoticeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}