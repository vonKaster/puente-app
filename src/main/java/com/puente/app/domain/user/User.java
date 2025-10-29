package com.puente.app.domain.user;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
    @Column(name="password_hash", nullable=false) private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="created_at", nullable=false) private Instant createdAt = Instant.now();
    @Column(name="updated_at", nullable=false) private Instant updatedAt = Instant.now();

    public User() {}
    public User(String name, String email, String passwordHash) {
        this.name = name; this.email = email; this.passwordHash = passwordHash;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void touch() { this.updatedAt = Instant.now(); }
}
