package com.github.afanas10101111.jtb.job;

import com.github.afanas10101111.jtb.service.NewArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewArticleJob {
    private final NewArticleService service;

    @Scheduled(fixedRateString = "${bot.job_start_interval}")
    public void findAndNotify() {
        log.info("findAndNotify -> Started searching for new articles");

        LocalDateTime start = LocalDateTime.now();
        service.findAndNotify();
        LocalDateTime end = LocalDateTime.now();

        log.info(
                "findAndNotify -> Search completed in {} seconds",
                end.toEpochSecond(ZoneOffset.UTC) - start.toEpochSecond(ZoneOffset.UTC)
        );
    }
}
