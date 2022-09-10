package be.podor.security.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByMember_Id(Long memberId);
}
