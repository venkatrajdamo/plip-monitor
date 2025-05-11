package com.plip.monitoring.service;

import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.repository.CheckResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckResultService {

    private final CheckResultRepository checkResultRepository;

    public List<CheckResult> getResultsForMonitor(Long monitorId) {
        return checkResultRepository.findByMonitorIdOrderByTimestampDesc(monitorId);
    }

    public CheckResult save(CheckResult checkResult) {
        return checkResultRepository.save(checkResult);
    }
}
