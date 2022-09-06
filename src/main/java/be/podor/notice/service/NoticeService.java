package be.podor.notice.service;

import be.podor.notice.dto.NoticeListResponseDto;
import be.podor.notice.model.Notice;
import be.podor.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Page<NoticeListResponseDto> getNotices(PageRequest pageRequest) {
        Page<Notice> pages = noticeRepository.findByOrderByCreatedAtDesc(pageRequest);

        List<NoticeListResponseDto> responseDtos = pages.stream()
                .map(NoticeListResponseDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDtos, pages.getPageable(), pages.getTotalElements());
    }
}
