package be.podor.member.repository;

import be.podor.member.model.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSearchRepository extends JpaRepository<MemberSearch, Long> {
}
