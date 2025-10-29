package com.puente.app.infrastructure.persistence.springdata;

import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.instrument.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PriceSnapshotJpaRepository extends JpaRepository<PriceSnapshot, Long> {
    @Query("SELECT ps FROM PriceSnapshot ps WHERE ps.instrument = :instrument ORDER BY ps.asOf DESC LIMIT 1")
    Optional<PriceSnapshot> findLatestByInstrument(@Param("instrument") Instrument instrument);
}

