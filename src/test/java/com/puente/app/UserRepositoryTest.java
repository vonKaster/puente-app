package com.puente.app;

import com.puente.app.domain.user.Role;
import com.puente.app.domain.user.RoleType;
import com.puente.app.domain.user.User;
import com.puente.app.infrastructure.persistence.springdata.RoleJpaRepository;
import com.puente.app.infrastructure.persistence.springdata.UserJpaRepository;
import com.puente.containers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestFlywayConfig.class)
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired UserJpaRepository users;
    @Autowired RoleJpaRepository roles;

    @Test
    void adminUserExistsAfterMigration() {
        var admin = users.findByEmail("fcaminos@puente.com");
        
        assertThat(admin).isPresent();
        assertThat(admin.get().getName()).isEqualTo("Franco Caminos");
        assertThat(admin.get().getEmail()).isEqualTo("fcaminos@puente.com");
        
        var userRoles = admin.get().getRoles();
        assertThat(userRoles).hasSize(2);
        assertThat(userRoles.stream().map(Role::getName))
            .containsExactlyInAnyOrder(RoleType.ADMIN, RoleType.USER);
    }

    @Test
    void rolesExistAfterMigration() {
        var userRole = roles.findByName(RoleType.USER);
        var adminRole = roles.findByName(RoleType.ADMIN);
        
        assertThat(userRole).isPresent();
        assertThat(adminRole).isPresent();
    }

    @Test
    void createNewUserWithUserRole() {
        Role userRole = roles.findByName(RoleType.USER)
            .orElseThrow(() -> new IllegalStateException("USER role not found"));

        User newUser = new User("Juan Pérez", "juan.perez@example.com", "$2b$12$hashedPassword");
        newUser.getRoles().add(userRole);
        
        users.save(newUser);

        assertThat(newUser.getId()).isNotNull();
        assertThat(users.findByEmail("juan.perez@example.com")).isPresent();
        assertThat(users.existsByEmail("juan.perez@example.com")).isTrue();
        
        var savedUser = users.findByEmail("juan.perez@example.com").get();
        assertThat(savedUser.getRoles()).hasSize(1);
        assertThat(savedUser.getRoles().iterator().next().getName()).isEqualTo(RoleType.USER);
    }

    @Test
    void cannotCreateUserWithDuplicateEmail() {
        assertThat(users.existsByEmail("fcaminos@puente.com")).isTrue();
        
        assertThat(users.existsByEmail("noexiste@example.com")).isFalse();
    }

    @Test
    void findAllUsersReturnsAtLeastAdminUser() {
        var allUsers = users.findAll();

        assertThat(allUsers).hasSizeGreaterThanOrEqualTo(1);

        assertThat(allUsers.stream().anyMatch(u -> u.getEmail().equals("fcaminos@puente.com")))
            .isTrue();
    }

    @Test
    void findByEmailIsCaseSensitive() {
        assertThat(users.findByEmail("fcaminos@puente.com")).isPresent();
        
        assertThat(users.findByEmail("FCAMINOS@PUENTE.COM")).isEmpty();
    }
}