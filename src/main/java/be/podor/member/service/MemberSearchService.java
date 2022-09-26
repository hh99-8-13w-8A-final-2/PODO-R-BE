package be.podor.member.service;

import be.podor.member.dto.membersearch.MemberSearchResponseDto;
import be.podor.member.model.MemberSearch;
import be.podor.member.repository.MemberSearchRepository;
import be.podor.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberSearchService {

    private static final int SEARCH_MAX = 5;

    private MemberSearchRepository memberSearchRepository;

    public MemberSearchResponseDto getRecentSearch(UserDetailsImpl userDetails) {
        MemberSearch memberSearch = memberSearchRepository.findByCreatedBy(userDetails.getMemberId())
                .orElseGet(MemberSearch::empty);

        List<String> recentSearches = Arrays.stream(memberSearch.getSearch().split(";"))
                .collect(Collectors.toList());

        return new MemberSearchResponseDto(recentSearches);
    }

    @Transactional
    public void appendSearch(String search, UserDetailsImpl userDetails) {
        MemberSearch memberSearch = memberSearchRepository.findByCreatedBy(userDetails.getMemberId())
                .orElseGet(MemberSearch::empty);

        LinkedHashSet<String> recentSearches = Arrays.stream(memberSearch.getSearch().split(";"))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        recentSearches.add(search);

        if (recentSearches.size() > SEARCH_MAX) {
            recentSearches.remove((recentSearches.iterator().next()));
        }

        memberSearch.updateSearch(String.join(";", recentSearches));
    }
}
