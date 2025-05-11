package com.plip.monitoring.service;

import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.MonitorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private static final Logger log = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private MonitorRepository monitorRepository;

    public Monitor createMonitor(Monitor monitor) {
        log.info("Creating new monitor: {}", monitor.getUrl());
        return monitorRepository.save(monitor);
    }

    public List<Monitor> getAllMonitors() {
        log.info("Fetching all monitors");
        return monitorRepository.findAll();
    }

    public Optional<Monitor> getMonitorById(Long id) {
        log.info("Fetching monitor by ID: {}", id);
        return monitorRepository.findById(id);
    }

    public void deleteMonitor(Long id) {
        log.info("Deleting monitor with ID: {}", id);
        monitorRepository.deleteById(id);
    }

    public Monitor updateMonitor(Long id, Monitor updatedMonitor) {
        log.info("Updating monitor with ID: {}", id);
        Monitor existing = monitorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Monitor with ID {} not found for update", id);
                    return new EntityNotFoundException("Monitor not found");
                });
        existing.setName(updatedMonitor.getName());
        existing.setUrl(updatedMonitor.getUrl());
        existing.setMethod(updatedMonitor.getMethod());
        existing.setIntervalSeconds(updatedMonitor.getIntervalSeconds());
        existing.setActive(updatedMonitor.isActive());
        return monitorRepository.save(existing);
    }
}