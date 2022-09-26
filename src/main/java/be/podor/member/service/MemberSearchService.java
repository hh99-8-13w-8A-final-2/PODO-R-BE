package be.podor.member.service;

import be.podor.member.dto.membersearch.MemberSearchRequestDto;
import be.podor.member.dto.membersearch.MemberSearchResponseDto;
import be.podor.member.model.MemberSearch;
import be.podor.member.repository.MemberSearchRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberSearchService {

    private static final int SEARCH_MAX = 5;

    private final MemberSearchRepository memberSearchRepository;

    public MemberSearchResponseDto getRecentSearch(UserDetailsImpl userDetails) {
        MemberSearch memberSearch = memberSearchRepository.findByCreatedBy(userDetails.getMemberId())
                .orElseGet(MemberSearch::empty);

        List<String> recentSearches = memberSearch.getSearch().isEmpty()
                ? new ArrayList<>()
                : Arrays.stream(memberSearch.getSearch().split(";")).collect(Collectors.toList());

        Collections.reverse(recentSearches);

        return new MemberSearchResponseDto(recentSearches);
    }

    public void appendSearch(MemberSearchRequestDto requestDto, UserDetailsImpl userDetails) {
        MemberSearch memberSearch = memberSearchRepository.findByCreatedBy(userDetails.getMemberId())
                .orElseGet(MemberSearch::empty);


        Set<String> recentSearches = memberSearch.getSearch().isEmpty()
                ? new LinkedHashSet<>()
                : Arrays.stream(memberSearch.getSearch().split(";"))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        recentSearches.add(requestDto.getRecent());

        if (recentSearches.size() > SEARCH_MAX) {
            recentSearches.remove((recentSearches.iterator().next()));
        }

        memberSearch.updateSearch(String.join(";", recentSearches));

        memberSearchRepository.save(memberSearch);
    }

    public void deleteSearch(MemberSearchRequestDto requestDto, UserDetailsImpl userDetails) {
        MemberSearch memberSearch = memberSearchRepository.findByCreatedBy(userDetails.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 검색 기록이 존재하지 않습니다."));

        Set<String> recentSearches = memberSearch.getSearch().isEmpty()
                ? new LinkedHashSet<>()
                : Arrays.stream(memberSearch.getSearch().split(";"))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        recentSearches.remove(requestDto.getRecent());

        memberSearch.updateSearch(String.join(";", recentSearches));

        memberSearchRepository.save(memberSearch);
    }
}
