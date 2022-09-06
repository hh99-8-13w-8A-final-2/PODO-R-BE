package be.podor.notice.model.controller;

import be.podor.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


}
