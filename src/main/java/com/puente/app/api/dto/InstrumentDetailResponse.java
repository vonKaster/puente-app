package com.puente.app.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class InstrumentDetailResponse {
    private Long id;
    private String symbol;
    private String name;
    private PriceData latestPrice;

    public InstrumentDetailResponse() {}

    public InstrumentDetailResponse(Long id, String symbol, String name, PriceData latestPrice) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.latestPrice = latestPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PriceData getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(PriceData latestPrice) {
        this.latestPrice = latestPrice;
    }

    public static class PriceData {
        private BigDecimal price;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private Long volume;
        private Instant asOf;

        public PriceData() {}

        public PriceData(BigDecimal price, BigDecimal open, BigDecimal high, BigDecimal low, Long volume, Instant asOf) {
            this.price = price;
            this.open = open;
            this.high = high;
            this.low = low;
            this.volume = volume;
            this.asOf = asOf;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }

        public Instant getAsOf() {
            return asOf;
        }

        public void setAsOf(Instant asOf) {
            this.asOf = asOf;
        }
    }
}

