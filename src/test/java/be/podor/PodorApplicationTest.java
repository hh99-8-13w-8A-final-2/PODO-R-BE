package be.podor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@EnableJpaAuditing
@SpringBootTest
class PodorApplicationTest {

    @Test
    void load() {
    }

}
