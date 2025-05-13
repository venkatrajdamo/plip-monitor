package com.plip.monitoring.service;

import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.repository.CheckResultRepository;
import com.plip.monitoring.model.MonitorHealthDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckResultService {

    private final CheckResultRepository checkResultRepository;
    private static final Logger log = LoggerFactory.getLogger(CheckResultService.class);

    public List<CheckResult> getResultsForMonitor(Long monitorId) {
        log.info("Fetching results for monitor ID: {}", monitorId);
        return checkResultRepository.findByMonitorIdOrderByTimestampDesc(monitorId);
    }

    public CheckResult save(CheckResult checkResult) {
        log.info("Saving check result for monitor ID: {}, status code: {}, response time: {} ms",
                checkResult.getMonitorId(),
                checkResult.getStatusCode(),
                checkResult.getResponseTimeMs());
        return checkResultRepository.save(checkResult);
    }

    public List<CheckResult> getResultsForMonitor(Long monitorId, int limit) {
        if (limit <= 0 || limit > 100) limit = 10; // fallback
        Pageable pageable = PageRequest.of(0, limit, Sort.by("timestamp").descending());
        return checkResultRepository.findByMonitorId(monitorId, pageable).getContent();
    }

    public MonitorHealthDto getHealthSummary(Long monitorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("timestamp").descending());
        List<CheckResult> results = checkResultRepository.findByMonitorId(monitorId, pageable).getContent();

        if (results.isEmpty()) {
            return new MonitorHealthDto("UNKNOWN", 0, 0, null);
        }

        long successCount = results.stream().filter(CheckResult::isSuccess).count();
        String status = results.get(0).isSuccess() ? "UP" : "DOWN";

        return new MonitorHealthDto(
                status,
                (successCount * 100.0) / results.size(),
                results.size(),
                results.get(0).getTimestamp()
        );
    }
}
