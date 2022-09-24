package be.podor.tag.service;

import be.podor.review.repository.ReviewTagRepository;
import be.podor.tag.dto.TagsResponseDto;
import be.podor.tag.model.Tag;
import be.podor.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    private final ReviewTagRepository reviewTagRepository;

    public TagsResponseDto getTagsStartWith(String tag) {
        List<Tag> tags = tagRepository.findTop10ByTagStartsWith(tag);

        return new TagsResponseDto(tags.stream()
                .map(Tag::getTag)
                .collect(Collectors.toList()));
    }

    @Cacheable(value = "popularTags", key = "#musicalId")
    public TagsResponseDto getPopularTags(Long musicalId, Pageable pageable) {
        List<String> tags = reviewTagRepository.findPopularTags(musicalId, pageable);

        return new TagsResponseDto(tags);
    }
}
