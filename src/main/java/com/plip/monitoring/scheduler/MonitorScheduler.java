package com.plip.monitoring.scheduler;

import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.MonitorRepository;
import com.plip.monitoring.service.CheckResultService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitorScheduler {

    private static final Logger log = LoggerFactory.getLogger(MonitorScheduler.class);
    private final MonitorRepository monitorRepository;
    private final CheckResultService checkResultService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedDelay = 60000)
    public void performHealthChecks() {
        log.info("Starting scheduled health checks");

        List<Monitor> activeMonitors = monitorRepository.findAll().stream()
                .filter(Monitor::isActive)
                .toList();

        for (Monitor monitor : activeMonitors) {
            long start = System.currentTimeMillis();
            boolean success = false;
            int statusCode = 0;

            try {
                var response = restTemplate.getForEntity(monitor.getUrl(), String.class);
                statusCode = response.getStatusCodeValue();
                success = response.getStatusCode().is2xxSuccessful();
                log.info("Checked URL {} - Status: {}", monitor.getUrl(), statusCode);
            } catch (RestClientException e) {
                log.warn("Failed to reach URL {}: {}", monitor.getUrl(), e.getMessage());
            }

            long duration = System.currentTimeMillis() - start;

            CheckResult result = new CheckResult();
            result.setMonitorId(monitor.getId());
            result.setTimestamp(LocalDateTime.now());
            result.setStatusCode(statusCode);
            result.setResponseTimeMs(duration);
            result.setSuccess(success);

            checkResultService.save(result);
        }

        log.info("Completed scheduled health checks");
    }
}
