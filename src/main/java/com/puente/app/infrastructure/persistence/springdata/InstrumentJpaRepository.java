package com.puente.app.infrastructure.persistence.springdata;

import com.puente.app.domain.instrument.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentJpaRepository extends JpaRepository<Instrument, Long> {
    Optional<Instrument> findBySymbol(String symbol);
    boolean existsBySymbol(String symbol);
}

