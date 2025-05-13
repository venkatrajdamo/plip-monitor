package com.plip.monitoring.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonitorHealthDto {
    private String status;
    private double uptimePercentage;
    private int totalChecks;
    private LocalDateTime lastChecked;
}
