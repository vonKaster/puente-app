package com.puente.app.api.mapper;

import com.puente.app.api.dto.FavoriteResponse;
import com.puente.app.domain.favorite.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

    private final InstrumentMapper instrumentMapper;

    public FavoriteMapper(InstrumentMapper instrumentMapper) {
        this.instrumentMapper = instrumentMapper;
    }

    public FavoriteResponse toResponse(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        return new FavoriteResponse(
            instrumentMapper.toResponse(favorite.getInstrument())
        );
    }
}

