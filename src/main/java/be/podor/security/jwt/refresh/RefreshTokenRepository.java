package be.podor.security.jwt.refresh;

import be.podor.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMember(Member member);
}
