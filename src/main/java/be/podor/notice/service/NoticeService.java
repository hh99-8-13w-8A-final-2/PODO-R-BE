package be.podor.notice.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.notice.dto.NoticeListResponseDto;
import be.podor.notice.dto.NoticeResponseDto;
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
    private final MemberRepository memberRepository;

    public Page<NoticeListResponseDto> getNotices(PageRequest pageRequest) {
        Page<Notice> pages = noticeRepository.findByOrderByCreatedAtDesc(pageRequest);

        List<NoticeListResponseDto> responseDtos = pages.stream()
                .map(NoticeListResponseDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDtos, pages.getPageable(), pages.getTotalElements());
    }

    public NoticeResponseDto getNoticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 게시글 입니다."));
        Member member = memberRepository.findById(notice.getCreatedBy()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 작성자입니다.")
        );
        return NoticeResponseDto.of(notice, MemberDto.of(member));
    }
}
