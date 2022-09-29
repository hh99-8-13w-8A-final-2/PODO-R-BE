package be.podor.notice.validator;

import be.podor.exception.podor.PodoalException;
import be.podor.notice.model.Notice;
import be.podor.notice.repository.NoticeRepository;

public class NoticeValidator {

    public static Notice validate(NoticeRepository noticeRepository, Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> PodoalException.NO_NOTICE_EXCEPTION);
    }

    public static Notice validate(NoticeRepository noticeRepository, Long noticeId, Long memberId ) {
        return noticeRepository.findByNoticeIdAndCreatedBy(noticeId, memberId)
                .orElseThrow(() -> PodoalException.NO_NOTICE_EXCEPTION);
    }
}
