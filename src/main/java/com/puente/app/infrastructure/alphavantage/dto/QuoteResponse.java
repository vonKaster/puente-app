package com.puente.app.infrastructure.alphavantage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {
    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("05. price")
    private String price;

    @JsonProperty("02. open")
    private String open;

    @JsonProperty("03. high")
    private String high;

    @JsonProperty("04. low")
    private String low;

    @JsonProperty("06. volume")
    private String volume;

    @JsonProperty("07. latest trading day")
    private String latestTradingDay;

    /**
     * Constructor vacío requerido por Jackson para deserialización JSON.
     */
    public QuoteResponse() {
        // Constructor usado por framework de deserialización
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLatestTradingDay() {
        return latestTradingDay;
    }

    public void setLatestTradingDay(String latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }
}

