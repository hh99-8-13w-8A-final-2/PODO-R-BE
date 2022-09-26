package be.podor.member.controller;

import be.podor.member.service.MemberSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;


}
