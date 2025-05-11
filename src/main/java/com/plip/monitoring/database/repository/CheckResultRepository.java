package com.plip.monitoring.database.repository;

import com.plip.monitoring.database.entity.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    List<CheckResult> findByMonitorIdOrderByTimestampDesc(Long monitorId);
}
