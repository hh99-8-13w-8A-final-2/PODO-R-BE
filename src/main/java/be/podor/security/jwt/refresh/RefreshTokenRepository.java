package be.podor.security.jwt.refresh;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMember_Id(Long memberId);

    @EntityGraph(attributePaths = {"member"})
    Optional<RefreshToken> findByTokenValue(String tokenValue);
}
