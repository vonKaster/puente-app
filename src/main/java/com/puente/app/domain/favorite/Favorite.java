package com.puente.app.domain.favorite;

import com.puente.app.domain.instrument.Instrument;
import com.puente.app.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "favorites")
@IdClass(Favorite.FavoriteId.class)
public class Favorite {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    public Favorite() {}

    public Favorite(User user, Instrument instrument) {
        this.user = user;
        this.instrument = instrument;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public static class FavoriteId implements Serializable {
        private Long user;
        private Long instrument;

        public FavoriteId() {}

        public FavoriteId(Long user, Long instrument) {
            this.user = user;
            this.instrument = instrument;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FavoriteId that = (FavoriteId) o;
            return Objects.equals(user, that.user) && Objects.equals(instrument, that.instrument);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, instrument);
        }
    }
}

