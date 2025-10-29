package com.puente.app.infrastructure.scheduler;

import com.puente.app.application.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PriceUpdateScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PriceUpdateScheduler.class);

    private final InstrumentService instrumentService;

    public PriceUpdateScheduler(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @Scheduled(fixedRate = 300000) // 5min
    public void updatePrices() {
        logger.info("Starting scheduled price update at {}", Instant.now());
        
        try {
            instrumentService.updateAllPrices();
            logger.info("Completed scheduled price update at {}", Instant.now());
        } catch (Exception e) {
            logger.error("Error during scheduled price update", e);
        }
    }
}

