package com.puente.app.infrastructure.alphavantage.dto;

public class AlphaVantageResult {
    private final QuoteResponse quoteResponse;
    private final boolean mocked;

    public AlphaVantageResult(QuoteResponse quoteResponse, boolean mocked) {
        this.quoteResponse = quoteResponse;
        this.mocked = mocked;
    }

    public QuoteResponse getQuoteResponse() {
        return quoteResponse;
    }

    public boolean isMocked() {
        return mocked;
    }
}

