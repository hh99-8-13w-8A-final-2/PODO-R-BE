package be.podor.tag.service;

import be.podor.tag.dto.TagsResponseDto;
import be.podor.tag.model.Tag;
import be.podor.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagsResponseDto getTagsStartWith(String tag) {
        List<Tag> tags = tagRepository.findTop10ByTagStartsWith(tag);

        return new TagsResponseDto(tags.stream()
                .map(Tag::getTag)
                .collect(Collectors.toList()));
    }
}
