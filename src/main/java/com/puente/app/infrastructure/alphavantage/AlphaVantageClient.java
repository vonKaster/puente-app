package com.puente.app.infrastructure.alphavantage;

import com.puente.app.infrastructure.alphavantage.dto.AlphaVantageResult;
import com.puente.app.infrastructure.alphavantage.dto.GlobalQuoteWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class AlphaVantageClient {

    private static final Logger logger = LoggerFactory.getLogger(AlphaVantageClient.class);
    private final WebClient webClient;
    private final String apiKey;
    private final MockDataProvider mockDataProvider;

    public AlphaVantageClient(WebClient.Builder webClientBuilder,
                            @Value("${alphavantage.api.key:demo}") String apiKey,
                            @Value("${alphavantage.api.base-url:https://www.alphavantage.co}") String baseUrl,
                            MockDataProvider mockDataProvider) {
        this.webClient = webClientBuilder
            .baseUrl(baseUrl)
            .build();
        this.apiKey = apiKey;
        this.mockDataProvider = mockDataProvider;
    }

    @Cacheable(value = "alphaVantageQuotes", key = "#symbol")
    public AlphaVantageResult getGlobalQuote(String symbol) {
        try {
            GlobalQuoteWrapper wrapper = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/query")
                    .queryParam("function", "GLOBAL_QUOTE")
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", apiKey)
                    .build())
                .retrieve()
                .bodyToMono(GlobalQuoteWrapper.class)
                .timeout(Duration.ofSeconds(10))
                .block();

            if (wrapper != null && wrapper.getGlobalQuote() != null) {
                logger.info("Real data from AlphaVantage for {}", symbol);
                return new AlphaVantageResult(wrapper.getGlobalQuote(), false);
            }
            
            logger.info("AlphaVantage rate limit or empty response for {}. Using fallback data.", symbol);
            return new AlphaVantageResult(mockDataProvider.getMockQuote(symbol), true);
        } catch (Exception e) {
            logger.info("AlphaVantage error for {}. Using fallback data.", symbol);
            return new AlphaVantageResult(mockDataProvider.getMockQuote(symbol), true);
        }
    }
}

