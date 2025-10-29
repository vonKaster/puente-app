package com.puente.app.api.controller;

import com.puente.app.api.dto.InstrumentDetailResponse;
import com.puente.app.api.dto.InstrumentResponse;
import com.puente.app.api.mapper.InstrumentMapper;
import com.puente.app.application.InstrumentService;
import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.instrument.PriceSnapshot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instruments")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Instrumentos Financieros", description = "Gestión de instrumentos financieros (acciones)")
public class InstrumentController {

    private final InstrumentService instrumentService;
    private final InstrumentMapper instrumentMapper;

    public InstrumentController(InstrumentService instrumentService, InstrumentMapper instrumentMapper) {
        this.instrumentService = instrumentService;
        this.instrumentMapper = instrumentMapper;
    }

    @GetMapping
    @Operation(
        summary = "Listar todos los instrumentos", 
        description = "Retorna la lista completa de los 20 instrumentos financieros disponibles"
    )
    public ResponseEntity<List<InstrumentResponse>> getAllInstruments() {
        List<InstrumentResponse> instruments = instrumentService.findAll().stream()
            .map(instrumentMapper::toResponse)
            .toList();
        return ResponseEntity.ok(instruments);
    }

    @GetMapping("/{symbol}")
    @Operation(
        summary = "Obtener detalles de instrumento", 
        description = "Retorna información detallada de un instrumento específico incluyendo su último precio actualizado. Los precios se actualizan automáticamente cada 5 minutos."
    )
    public ResponseEntity<InstrumentDetailResponse> getInstrumentBySymbol(@PathVariable String symbol) {
        Instrument instrument = instrumentService.findBySymbol(symbol)
            .orElseThrow(() -> new IllegalArgumentException("Instrumento no encontrado: " + symbol));
        
        Optional<PriceSnapshot> latestPrice = instrumentService.getLatestPrice(symbol);
        
        InstrumentDetailResponse response = instrumentMapper.toDetailResponse(instrument, latestPrice);
        return ResponseEntity.ok(response);
    }
}

