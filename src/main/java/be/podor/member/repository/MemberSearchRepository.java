package be.podor.member.repository;

import be.podor.member.model.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberSearchRepository extends JpaRepository<MemberSearch, Long> {

    Optional<MemberSearch> findByCreatedBy(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE MemberSearch s SET s.createdBy = :leaveMemberId WHERE s.createdBy = :createdBy")
    void updateCreatedBy(Long leaveMemberId, Long createdBy);
}
