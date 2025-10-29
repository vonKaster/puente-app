package com.puente.app.api.mapper;

import com.puente.app.api.dto.InstrumentDetailResponse;
import com.puente.app.api.dto.InstrumentResponse;
import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.instrument.PriceSnapshot;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InstrumentMapper {

    public InstrumentResponse toResponse(Instrument instrument) {
        if (instrument == null) {
            return null;
        }

        return new InstrumentResponse(
            instrument.getId(),
            instrument.getSymbol(),
            instrument.getName()
        );
    }

    public InstrumentDetailResponse toDetailResponse(Instrument instrument, Optional<PriceSnapshot> latestPrice) {
        if (instrument == null) {
            return null;
        }

        InstrumentDetailResponse.PriceData priceData = latestPrice.map(ps -> 
            new InstrumentDetailResponse.PriceData(
                ps.getPrice(),
                ps.getOpen(),
                ps.getHigh(),
                ps.getLow(),
                ps.getVolume(),
                ps.getAsOf()
            )
        ).orElse(null);

        return new InstrumentDetailResponse(
            instrument.getId(),
            instrument.getSymbol(),
            instrument.getName(),
            priceData
        );
    }
}

