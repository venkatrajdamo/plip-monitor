package com.plip.monitoring.unit.service;

import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.MonitorRepository;
import com.plip.monitoring.service.MonitorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonitorServiceTest {

    @Mock
    private MonitorRepository monitorRepository;

    @InjectMocks
    private MonitorService monitorService;

    private Monitor monitor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        monitor = new Monitor(1L, "Test Monitor", "http://example.com", "GET", 60, true);
    }

    @Test
    void testCreateMonitor() {
        when(monitorRepository.save(any(Monitor.class))).thenReturn(monitor);
        Monitor created = monitorService.createMonitor(monitor);
        assertEquals("Test Monitor", created.getName());
        verify(monitorRepository, times(1)).save(monitor);
    }

    @Test
    void testGetAllMonitors() {
        List<Monitor> monitors = List.of(monitor);
        when(monitorRepository.findAll()).thenReturn(monitors);
        List<Monitor> result = monitorService.getAllMonitors();
        assertEquals(1, result.size());
        verify(monitorRepository).findAll();
    }

    @Test
    void testGetMonitorById_Found() {
        when(monitorRepository.findById(1L)).thenReturn(Optional.of(monitor));
        Optional<Monitor> result = monitorService.getMonitorById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Monitor", result.get().getName());
    }

    @Test
    void testGetMonitorById_NotFound() {
        when(monitorRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Monitor> result = monitorService.getMonitorById(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateMonitor_Success() {
        Monitor updated = new Monitor(1L, "Updated", "http://updated.com", "POST", 30, false);
        when(monitorRepository.findById(1L)).thenReturn(Optional.of(monitor));
        when(monitorRepository.save(any(Monitor.class))).thenReturn(updated);

        Monitor result = monitorService.updateMonitor(1L, updated);
        assertEquals("Updated", result.getName());
        verify(monitorRepository).save(monitor);
    }

    @Test
    void testUpdateMonitor_NotFound() {
        Monitor updated = new Monitor(99L, "Updated", "http://updated.com", "POST", 30, false);
        when(monitorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            monitorService.updateMonitor(99L, updated);
        });
    }

    @Test
    void testDeleteMonitor() {
        doNothing().when(monitorRepository).deleteById(1L);
        monitorService.deleteMonitor(1L);
        verify(monitorRepository).deleteById(1L);
    }
}