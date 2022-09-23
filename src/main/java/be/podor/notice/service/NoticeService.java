package be.podor.notice.service;

import be.podor.member.dto.MemberDto;
import be.podor.member.model.Member;
import be.podor.member.repository.MemberRepository;
import be.podor.notice.dto.NoticeListResponseDto;
import be.podor.notice.dto.NoticeRequestDto;
import be.podor.notice.dto.NoticeResponseDto;
import be.podor.notice.model.Notice;
import be.podor.notice.repository.NoticeRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(notice.getCreatedBy())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작성자입니다."));
        return NoticeResponseDto.of(notice, MemberDto.of(member));
    }

    @Transactional
    public Notice createNotice(NoticeRequestDto requestDto) {
        Notice notice = Notice.of(requestDto);
        noticeRepository.save(notice);
        return notice;
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto, UserDetailsImpl userDetails) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if (!notice.getCreatedBy().equals(userDetails.getMemberId())) {
            throw new IllegalArgumentException("공지사항을 수정할 수 없습니다.");
        }
        notice.update(requestDto);
        Member member = memberRepository.findById(notice.getCreatedBy())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작성자입니다.")
        );
        return NoticeResponseDto.of(notice, MemberDto.of(member));
    }

    @Transactional
    public void deleteNotice(Long noticeId, UserDetailsImpl userDetails) {
        Notice notice = noticeRepository.findByNoticeIdAndCreatedBy(noticeId, userDetails.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작성자입니다."));
        noticeRepository.delete(notice);
    }
}
