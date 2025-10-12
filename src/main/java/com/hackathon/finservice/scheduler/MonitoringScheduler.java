package com.hackathon.finservice.scheduler;

import com.hackathon.finservice.service.MonitoringService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonitoringScheduler {
    private final MonitoringService monitoringService;

    public MonitoringScheduler(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Scheduled(fixedRate = 15000)
    public void executeMonitoring() {
        monitoringService.runMonitoringCycle();
    }
}
