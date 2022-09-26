package be.podor.member.controller;

import be.podor.member.dto.membersearch.MemberSearchRequestDto;
import be.podor.member.dto.membersearch.MemberSearchResponseDto;
import be.podor.member.service.MemberSearchService;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    @GetMapping("/api/recents/search")
    public ResponseEntity<?> getRecentSearch(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MemberSearchResponseDto responseDto = memberSearchService.getRecentSearch(userDetails);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/recents/search")
    public ResponseEntity<?> postRecentSearch(
            @RequestBody MemberSearchRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        memberSearchService.appendSearch(requestDto, userDetails);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/recents/search")
    public ResponseEntity<?> deleteRecentSearch(
            @RequestBody MemberSearchRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        memberSearchService.deleteSearch(requestDto, userDetails);

        return ResponseEntity.ok().build();
    }
}
