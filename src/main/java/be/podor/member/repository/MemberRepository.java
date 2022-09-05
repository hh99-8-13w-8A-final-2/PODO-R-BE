package be.podor.member.repository;

import be.podor.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoId(Long kakaoId);
    Optional<Member> findByTwitterId(String twitterId);
}