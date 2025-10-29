package com.puente.app.application;

import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.instrument.PriceSnapshot;
import com.puente.app.infrastructure.alphavantage.AlphaVantageClient;
import com.puente.app.infrastructure.alphavantage.dto.AlphaVantageResult;
import com.puente.app.infrastructure.alphavantage.dto.QuoteResponse;
import com.puente.app.infrastructure.persistence.springdata.InstrumentJpaRepository;
import com.puente.app.infrastructure.persistence.springdata.PriceSnapshotJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InstrumentService {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentService.class);
    private final InstrumentJpaRepository instrumentRepository;
    private final PriceSnapshotJpaRepository priceSnapshotRepository;
    private final AlphaVantageClient alphaVantageClient;

    public InstrumentService(InstrumentJpaRepository instrumentRepository,
                           PriceSnapshotJpaRepository priceSnapshotRepository,
                           AlphaVantageClient alphaVantageClient) {
        this.instrumentRepository = instrumentRepository;
        this.priceSnapshotRepository = priceSnapshotRepository;
        this.alphaVantageClient = alphaVantageClient;
    }

    @Transactional(readOnly = true)
    public List<Instrument> findAll() {
        return instrumentRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "instrumentDetails", key = "#symbol")
    public Optional<Instrument> findBySymbol(String symbol) {
        return instrumentRepository.findBySymbol(symbol);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "instrumentPrice", key = "#symbol")
    public Optional<PriceSnapshot> getLatestPrice(String symbol) {
        Optional<Instrument> instrument = instrumentRepository.findBySymbol(symbol);
        if (instrument.isEmpty()) {
            return Optional.empty();
        }
        return priceSnapshotRepository.findLatestByInstrument(instrument.get());
    }

    public void updatePriceForSymbol(String symbol) {
        Optional<Instrument> instrumentOpt = instrumentRepository.findBySymbol(symbol);
        if (instrumentOpt.isEmpty()) {
            return;
        }

        Instrument instrument = instrumentOpt.get();
        
        try {
            AlphaVantageResult result = alphaVantageClient.getGlobalQuote(symbol);
            QuoteResponse quote = result.getQuoteResponse();
            
            PriceSnapshot snapshot = new PriceSnapshot();
            snapshot.setInstrument(instrument);
            snapshot.setPrice(new BigDecimal(quote.getPrice()));
            snapshot.setOpen(new BigDecimal(quote.getOpen()));
            snapshot.setHigh(new BigDecimal(quote.getHigh()));
            snapshot.setLow(new BigDecimal(quote.getLow()));
            snapshot.setVolume(Long.parseLong(quote.getVolume()));
            snapshot.setAsOf(Instant.now());
            snapshot.setMockedData(result.isMocked());
            
            priceSnapshotRepository.save(snapshot);
        } catch (Exception e) {
            logger.error("Failed to update price for {}: {}", symbol, e.getMessage());
        }
    }

    public void updateAllPrices() {
        List<Instrument> instruments = instrumentRepository.findAll();
        for (Instrument instrument : instruments) {
            updatePriceForSymbol(instrument.getSymbol());
            
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

