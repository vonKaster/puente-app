package com.puente.app.infrastructure.persistence.springdata;

import com.puente.app.domain.user.Role;
import com.puente.app.domain.user.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
