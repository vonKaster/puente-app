package com.puente.app.infrastructure.alphavantage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puente.app.infrastructure.alphavantage.dto.QuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(MockDataProvider.class);
    private final Map<String, QuoteResponse> mockData = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void loadMockData() {
        try {
            ClassPathResource resource = new ClassPathResource("mock-data/financial-instruments.json");
            JsonNode root = objectMapper.readTree(resource.getInputStream());
            JsonNode instruments = root.get("instruments");

            instruments.fields().forEachRemaining(entry -> {
                String symbol = entry.getKey();
                JsonNode data = entry.getValue();

                QuoteResponse mock = new QuoteResponse();
                mock.setSymbol(data.get("symbol").asText());
                mock.setPrice(data.get("price").asText());
                mock.setOpen(data.get("open").asText());
                mock.setHigh(data.get("high").asText());
                mock.setLow(data.get("low").asText());
                mock.setVolume(data.get("volume").asText());
                mock.setLatestTradingDay(LocalDate.now().toString());

                mockData.put(symbol, mock);
            });

            logger.info("Loaded mock data for {} instruments", mockData.size());
        } catch (IOException e) {
            logger.error("Failed to load mock data", e);
        }
    }

    public QuoteResponse getMockQuote(String symbol) {
        QuoteResponse mock = mockData.get(symbol);
        if (mock != null) {
            return mock;
        }

        // Fallback genérico si no existe el símbolo
        QuoteResponse fallback = new QuoteResponse();
        fallback.setSymbol(symbol);
        fallback.setPrice("100.00");
        fallback.setOpen("99.50");
        fallback.setHigh("101.00");
        fallback.setLow("99.00");
        fallback.setVolume("1000000");
        fallback.setLatestTradingDay(LocalDate.now().toString());
        return fallback;
    }

    public boolean hasMockData(String symbol) {
        return mockData.containsKey(symbol);
    }
}

