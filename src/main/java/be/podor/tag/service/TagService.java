package be.podor.tag.service;

import be.podor.redis.CacheKey;
import be.podor.review.repository.ReviewTagRepository;
import be.podor.tag.dto.TagsResponseDto;
import be.podor.tag.model.Tag;
import be.podor.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Cacheable(value = CacheKey.Key.TAG_KEY, key = "#musicalId")
    public TagsResponseDto getPopularTags(Long musicalId, Pageable pageable) {
        List<String> tags = new ArrayList<>();

        tags.add("#시야방해있음");
        tags.add("#오페라글라스필수");
        tags.addAll(reviewTagRepository.findPopularTags(musicalId, pageable));

        return new TagsResponseDto(tags);
    }
}
