package be.podor.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheScheduler {

    // 30분마다
    @Scheduled(cron = "0 */30 * * * *")
    @CacheEvict(value = "musicals", key = "'popular'")
    public void refreshPopularMusicals() {
        log.info("popular musical cache refresh");
    }

    // 10분마다
    @Scheduled(cron = "0 */10 * * * *")
    @CacheEvict(value = "popularTags", allEntries = true)
    public void refreshPopularTags() {
        log.info("open musical popular tag cache refresh");
    }
}
