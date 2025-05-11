package com.plip.monitoring.service;

import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.repository.CheckResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
