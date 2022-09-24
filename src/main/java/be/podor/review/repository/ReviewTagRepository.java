package be.podor.review.repository;

import be.podor.review.model.tag.ReviewTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {

    @Query(value = "SELECT rt.tag.tag FROM ReviewTag rt " +
            "INNER JOIN rt.review AS r " +
            "WHERE r.musical.musicalId = :musicalId " +
            "GROUP BY rt.tag " +
            "ORDER BY COUNT(rt.tag) DESC")
    List<String> findPopularTags(Long musicalId, Pageable pageable);
}
