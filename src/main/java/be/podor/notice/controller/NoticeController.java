package be.podor.notice.controller;

import be.podor.notice.dto.NoticeListResponseDto;
import be.podor.notice.dto.NoticeResponseDto;
import be.podor.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/api/notices")
    public ResponseEntity<?> getNotices(
            @PageableDefault(size = 4, page = 1) Pageable pageable
    ) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        Page<NoticeListResponseDto> responseDto = noticeService.getNotices(pageRequest);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/api/notices/{noticeId}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeResponseDto responseDto = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(responseDto);
    }
}
