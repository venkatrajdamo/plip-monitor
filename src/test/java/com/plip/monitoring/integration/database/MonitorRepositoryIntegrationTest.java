package com.plip.monitoring.integration.database;

import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.MonitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class MonitorRepositoryIntegrationTest {

    @Autowired
    private MonitorRepository monitorRepository;

    @BeforeEach
    void clearDatabase() {
        monitorRepository.deleteAll();
    }

    @Test
    void testSaveMonitor() {
        Monitor monitor = new Monitor(null, "Check A", "http://example.com", "GET", 30, true);
        Monitor saved = monitorRepository.save(monitor);
        assertNotNull(saved.getId());
    }

    @Test
    void testFindByIdAfterSave() {
        Monitor monitor = new Monitor(null, "Check B", "http://example.com", "GET", 60, true);
        Monitor saved = monitorRepository.save(monitor);
        Optional<Monitor> retrieved = monitorRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Check B", retrieved.get().getName());
    }
}