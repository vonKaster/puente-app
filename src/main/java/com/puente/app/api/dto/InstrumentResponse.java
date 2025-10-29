package com.puente.app.api.dto;

public class InstrumentResponse {
    private Long id;
    private String symbol;
    private String name;

    public InstrumentResponse() {}

    public InstrumentResponse(Long id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
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
}

