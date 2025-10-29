package com.puente.app.infrastructure.alphavantage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalQuoteWrapper {
    @JsonProperty("Global Quote")
    private QuoteResponse globalQuote;

    public GlobalQuoteWrapper() {}

    public QuoteResponse getGlobalQuote() {
        return globalQuote;
    }

    public void setGlobalQuote(QuoteResponse globalQuote) {
        this.globalQuote = globalQuote;
    }
}

