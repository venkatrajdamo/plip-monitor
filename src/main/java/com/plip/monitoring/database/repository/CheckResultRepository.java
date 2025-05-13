package com.plip.monitoring.database.repository;

import com.plip.monitoring.database.entity.CheckResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    Page<CheckResult> findByMonitorId(Long monitorId, Pageable pageable);
    List<CheckResult> findByMonitorIdOrderByTimestampDesc(Long monitorId);
    List<CheckResult> findTop10ByMonitorIdOrderByTimestampDesc(Long monitorId);
    List<CheckResult> findTop20ByMonitorIdOrderByTimestampDesc(Long monitorId);
}
