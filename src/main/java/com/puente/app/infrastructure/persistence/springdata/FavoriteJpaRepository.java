package com.puente.app.infrastructure.persistence.springdata;

import com.puente.app.domain.favorite.Favorite;
import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Favorite.FavoriteId> {
    @Query("SELECT f FROM Favorite f WHERE f.user = :user")
    List<Favorite> findByUser(@Param("user") User user);

    @Query("SELECT f FROM Favorite f WHERE f.user = :user AND f.instrument = :instrument")
    Optional<Favorite> findByUserAndInstrument(@Param("user") User user, @Param("instrument") Instrument instrument);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Favorite f WHERE f.user = :user AND f.instrument = :instrument")
    boolean existsByUserAndInstrument(@Param("user") User user, @Param("instrument") Instrument instrument);
}

