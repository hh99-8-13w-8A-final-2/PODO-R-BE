package be.podor.member.controller;

import be.podor.member.dto.membersearch.MemberSearchResponseDto;
import be.podor.member.service.MemberSearchService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    @GetMapping("/api/recents")
    public ResponseEntity<?> getRecentSearch(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MemberSearchResponseDto responseDto = memberSearchService.getRecentSearch(userDetails);

        return ResponseEntity.ok(responseDto);
    }
}
