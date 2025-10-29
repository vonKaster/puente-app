package com.puente.app.application;

import com.puente.app.domain.favorite.Favorite;
import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.user.User;
import com.puente.app.infrastructure.persistence.springdata.FavoriteJpaRepository;
import com.puente.app.infrastructure.persistence.springdata.InstrumentJpaRepository;
import com.puente.app.infrastructure.persistence.springdata.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private static final String USER_NOT_FOUND = "User not found";
    
    private final FavoriteJpaRepository favoriteRepository;
    private final UserJpaRepository userRepository;
    private final InstrumentJpaRepository instrumentRepository;
    private final AuditService auditService;

    public FavoriteService(FavoriteJpaRepository favoriteRepository,
                         UserJpaRepository userRepository,
                         InstrumentJpaRepository instrumentRepository,
                         AuditService auditService) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.auditService = auditService;
    }

    public Favorite addFavorite(Long userId, String symbol) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        Instrument instrument = instrumentRepository.findBySymbol(symbol)
            .orElseThrow(() -> new IllegalArgumentException("Instrument not found: " + symbol));

        if (favoriteRepository.existsByUserAndInstrument(user, instrument)) {
            throw new IllegalArgumentException("Instrument already in favorites");
        }

        Favorite favorite = new Favorite(user, instrument);
        Favorite saved = favoriteRepository.save(favorite);
        
        auditService.log("FAVORITE_ADDED", user.getEmail(), "Added favorite: " + symbol);
        return saved;
    }

    public void removeFavorite(Long userId, String symbol) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        Instrument instrument = instrumentRepository.findBySymbol(symbol)
            .orElseThrow(() -> new IllegalArgumentException("Instrument not found: " + symbol));

        Favorite favorite = favoriteRepository.findByUserAndInstrument(user, instrument)
            .orElseThrow(() -> new IllegalArgumentException("Favorite not found"));

        favoriteRepository.delete(favorite);
        auditService.log("FAVORITE_REMOVED", user.getEmail(), "Removed favorite: " + symbol);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getFavorites(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));
        
        return favoriteRepository.findByUser(user);
    }
}

