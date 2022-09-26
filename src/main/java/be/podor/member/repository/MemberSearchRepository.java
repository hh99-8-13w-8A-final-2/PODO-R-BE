package be.podor.member.repository;

import be.podor.member.model.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSearchRepository extends JpaRepository<MemberSearch, Long> {

    Optional<MemberSearch> findByCreatedBy(Long memberId);

}
