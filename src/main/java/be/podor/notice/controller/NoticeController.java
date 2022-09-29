package be.podor.notice.controller;

import be.podor.notice.dto.NoticeListResponseDto;
import be.podor.notice.dto.NoticeRequestDto;
import be.podor.notice.dto.NoticeResponseDto;
import be.podor.notice.model.Notice;
import be.podor.notice.service.NoticeService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Secured("ROLE_ADMIN")
    @PostMapping("/api/notices")
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequestDto requestDto) {
        Notice notice = noticeService.createNotice(requestDto);
        return ResponseEntity.ok().build();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/api/notices/{noticeId}")
    public ResponseEntity<?> updateNotice(@PathVariable Long noticeId,
                                          @RequestBody NoticeRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NoticeResponseDto responseDto = noticeService.updateNotice(noticeId, requestDto, userDetails);
        return ResponseEntity.ok(responseDto);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/api/notices/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        noticeService.deleteNotice(noticeId, userDetails);
        return ResponseEntity.ok().build();
    }
}
