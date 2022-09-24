package be.podor.scheduler;

import be.podor.musical.service.MusicalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheScheduler {

    private final MusicalService musicalService;

    // 매 시 0분, 30분
    @Scheduled(cron = "0 0/30 * * * *")
    @CacheEvict(value = "musicals", key = "'popular'")
    public void refreshPopularMusicals() {
        log.info("popular musical cache refresh");
    }

    // 10분마다
    @Scheduled(cron = "0 */10 * * * *")
    public void refreshPopularTags() {
        log.info("open musical popular tag cache refresh");

        musicalService.getOpenMusical().forEach(musical -> evictPopularTagCache(musical.getMusicalId()));
    }

    @CacheEvict(value = "popularTags", key = "#musicalId")
    public void evictPopularTagCache(Long musicalId) {

    }
}
