package com.puente.app.api.dto;

public class FavoriteResponse {
    private InstrumentResponse instrument;

    public FavoriteResponse() {}

    public FavoriteResponse(InstrumentResponse instrument) {
        this.instrument = instrument;
    }

    public InstrumentResponse getInstrument() {
        return instrument;
    }

    public void setInstrument(InstrumentResponse instrument) {
        this.instrument = instrument;
    }
}

