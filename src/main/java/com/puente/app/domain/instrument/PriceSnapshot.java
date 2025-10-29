package com.puente.app.domain.instrument;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "price_snapshot")
public class PriceSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal price;

    @Column(precision = 18, scale = 6)
    private BigDecimal open;

    @Column(precision = 18, scale = 6)
    private BigDecimal high;

    @Column(precision = 18, scale = 6)
    private BigDecimal low;

    @Column
    private Long volume;

    @Column(name = "as_of", nullable = false)
    private Instant asOf;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "mocked_data", nullable = false)
    private Boolean mockedData = false;

    public PriceSnapshot() {}

    public PriceSnapshot(Instrument instrument, BigDecimal price, Instant asOf) {
        this.instrument = instrument;
        this.price = price;
        this.asOf = asOf;
    }

    public Long getId() {
        return id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Boolean getMockedData() {
        return mockedData;
    }

    public void setMockedData(Boolean mockedData) {
        this.mockedData = mockedData;
    }
}

