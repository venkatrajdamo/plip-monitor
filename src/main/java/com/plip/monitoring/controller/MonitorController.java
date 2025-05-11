package com.plip.monitoring.controller;


import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.service.CheckResultService;
import com.plip.monitoring.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitors")
@RequiredArgsConstructor
public class MonitorController {

    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private CheckResultService checkResultService;

    @PostMapping
    public ResponseEntity<Monitor> create(@RequestBody Monitor monitor) {
        log.info("Creating monitor with URL: {}", monitor.getUrl());
        Monitor saved = monitorService.createMonitor(monitor);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Monitor> getAll() {
        log.info("Fetching all monitors");
        return monitorService.getAllMonitors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monitor> getById(@PathVariable Long id) {
        log.info("Fetching monitor by ID: {}", id);
        return monitorService.getMonitorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting monitor with ID: {}", id);
        monitorService.deleteMonitor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monitor> update(@PathVariable Long id, @RequestBody Monitor monitor) {
        log.info("Updating monitor with ID: {}", id);
        Monitor updated = monitorService.updateMonitor(id, monitor);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<CheckResult>> getMonitorResults(@PathVariable Long id) {
        log.info("Fetching check results for monitor ID: {}", id);
        return ResponseEntity.ok(checkResultService.getResultsForMonitor(id));
    }
}