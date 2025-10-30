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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class MockDataProvider {

    private static final Logger logger = LoggerFactory.getLogger(MockDataProvider.class);
    private final Map<String, List<QuoteResponse>> mockData = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @PostConstruct
    public void loadMockData() {
        try {
            ClassPathResource resource = new ClassPathResource("mock-data/financial-instruments.json");
            JsonNode root = objectMapper.readTree(resource.getInputStream());
            JsonNode instruments = root.get("instruments");

            instruments.fields().forEachRemaining(entry -> {
                String symbol = entry.getKey();
                JsonNode dataArray = entry.getValue();
                
                List<QuoteResponse> quotes = new ArrayList<>();
                
                dataArray.forEach(data -> {
                    QuoteResponse mock = new QuoteResponse();
                    mock.setSymbol(data.get("symbol").asText());
                    mock.setPrice(data.get("price").asText());
                    mock.setOpen(data.get("open").asText());
                    mock.setHigh(data.get("high").asText());
                    mock.setLow(data.get("low").asText());
                    mock.setVolume(data.get("volume").asText());
                    mock.setLatestTradingDay(LocalDate.now().toString());
                    
                    quotes.add(mock);
                });

                mockData.put(symbol, quotes);
            });

            int totalVariations = mockData.values().stream()
                .mapToInt(List::size)
                .sum();
            logger.info("Loaded mock data for {} instruments with {} total variations", 
                mockData.size(), totalVariations);
        } catch (IOException e) {
            logger.error("Failed to load mock data", e);
        }
    }

    public QuoteResponse getMockQuote(String symbol) {
        List<QuoteResponse> quotes = mockData.get(symbol);
        if (quotes != null && !quotes.isEmpty()) {
            int index = random.nextInt(quotes.size());
            QuoteResponse selected = quotes.get(index);
            
            QuoteResponse copy = new QuoteResponse();
            copy.setSymbol(selected.getSymbol());
            copy.setPrice(selected.getPrice());
            copy.setOpen(selected.getOpen());
            copy.setHigh(selected.getHigh());
            copy.setLow(selected.getLow());
            copy.setVolume(selected.getVolume());
            copy.setLatestTradingDay(LocalDate.now().toString());
            
            return copy;
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

