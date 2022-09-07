package be.podor.review.repository;

import be.podor.review.model.tag.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, String> {

    @EntityGraph(attributePaths = {"reviewTags"}, type = EntityGraph.EntityGraphType.LOAD)
    Set<Tag> findByTagIn(List<String> tags);
}
