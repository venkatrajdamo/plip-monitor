package com.plip.monitoring.service;

import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.MonitorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;

    public Monitor createMonitor(Monitor monitor) {
        return monitorRepository.save(monitor);
    }

    public List<Monitor> getAllMonitors() {
        return monitorRepository.findAll();
    }

    public Optional<Monitor> getMonitorById(Long id) {
        return monitorRepository.findById(id);
    }

    public void deleteMonitor(Long id) {
        monitorRepository.deleteById(id);
    }

    public Monitor updateMonitor(Long id, Monitor updatedMonitor) {
        Monitor existing = monitorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Monitor not found"));
        existing.setName(updatedMonitor.getName());
        existing.setUrl(updatedMonitor.getUrl());
        existing.setMethod(updatedMonitor.getMethod());
        existing.setIntervalSeconds(updatedMonitor.getIntervalSeconds());
        existing.setActive(updatedMonitor.isActive());
        return monitorRepository.save(existing);
    }
}
