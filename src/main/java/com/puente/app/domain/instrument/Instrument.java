package com.puente.app.domain.instrument;

import jakarta.persistence.*;

@Entity
@Table(name = "instruments")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String symbol;

    @Column(nullable = false, length = 120)
    private String name;

    public Instrument() {}

    public Instrument(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public Long getId() {
        return id;
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

