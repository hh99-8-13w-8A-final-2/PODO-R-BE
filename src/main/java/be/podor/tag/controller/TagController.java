package be.podor.tag.controller;

import be.podor.tag.dto.TagsResponseDto;
import be.podor.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public ResponseEntity<?> getTagsStartWith(@RequestParam(name = "tag", defaultValue = "") String tag) {
        TagsResponseDto responseDto = tagService.getTagsStartWith(tag);

        return ResponseEntity.ok(responseDto);
    }
}
