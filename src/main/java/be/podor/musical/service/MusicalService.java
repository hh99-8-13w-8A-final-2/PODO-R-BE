package be.podor.musical.service;

import be.podor.musical.dto.MusicalListResponseDto;
import be.podor.musical.dto.MusicalResponseDto;
import be.podor.musical.model.Musical;
import be.podor.musical.repository.MusicalRepository;
import be.podor.musical.validator.MusicalValidator;
import be.podor.redis.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicalService {

    private final MusicalRepository musicalRepository;

    private static PageRequest POPULAR_LIMIT = PageRequest.of(0, 2);

    // 메인화면 상영중 뮤지컬 가져오기
    @Cacheable(value = CacheKey.Key.MUSICAL_KEY, key = "'open'")
    public List<MusicalListResponseDto> getOpenMusical() {
        List<Musical> musicals = musicalRepository.findTop10ByOrderByOpenDateDesc();

        return musicals.stream()
                .map(MusicalListResponseDto::of)
                .collect(Collectors.toList());
    }

    // 전체 뮤지컬 가져오기
    @Cacheable(value = CacheKey.Key.MUSICAL_KEY, key = "'all'")
    public List<MusicalListResponseDto> getAllMusical() {
        List<Musical> musicals = musicalRepository.findTop15ByOrderByOpenDateDesc();

        return musicals.stream()
                .map(MusicalListResponseDto::of)
                .collect(Collectors.toList());
    }

    // 가장 리뷰가 많은 뮤지컬 가져오기
    @Cacheable(value = CacheKey.Key.MUSICAL_KEY, key = "'popular'")
    public List<MusicalListResponseDto> getPopularMusical() {
        List<Musical> musicals = musicalRepository.findPopularMusical(POPULAR_LIMIT);

        return musicals.stream()
                .map(MusicalListResponseDto::of)
                .collect(Collectors.toList());
    }

    // 선택된 뮤지컬 가져오기
    public MusicalResponseDto getMusical(Long musicalId) {
        Musical musical = MusicalValidator.validate(musicalRepository, musicalId);
        return MusicalResponseDto.of(musical);
    }
}
