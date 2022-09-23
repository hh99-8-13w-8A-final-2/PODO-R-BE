package be.podor.notice.repository;

import be.podor.notice.model.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByOrderByCreatedAtDesc(PageRequest pageRequest);

    Optional<Notice> findByNoticeIdAndCreatedBy(Long noticeId, Long memberId);
}
