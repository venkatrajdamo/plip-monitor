package com.plip.monitoring.integration.controller;

import com.plip.monitoring.database.entity.CheckResult;
import com.plip.monitoring.database.entity.Monitor;
import com.plip.monitoring.database.repository.CheckResultRepository;
import com.plip.monitoring.database.repository.MonitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MonitorHealthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private MonitorRepository monitorRepository;
    @Autowired private CheckResultRepository checkResultRepository;

    @BeforeEach
    void setup() {
        monitorRepository.deleteAll();
        checkResultRepository.deleteAll();
    }

    @Test
    void shouldReturnHealthSummary() throws Exception {
        Monitor monitor = monitorRepository.save(new Monitor(null, "Test", "http://localhost", "GET", 30, true));

        for (int i = 0; i < 5; i++) {
            checkResultRepository.save(new CheckResult(null, monitor.getId(), LocalDateTime.now().minusMinutes(i), 200, 120, true));
        }

        for (int i = 0; i < 3; i++) {
            checkResultRepository.save(new CheckResult(null, monitor.getId(), LocalDateTime.now().minusMinutes(10 + i), 500, 300, false));
        }

        mockMvc.perform(get("/api/monitors/" + monitor.getId() + "/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uptimePercentage").value(62.5));
    }
}