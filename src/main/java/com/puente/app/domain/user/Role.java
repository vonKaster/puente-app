package com.puente.app.domain.user;

import jakarta.persistence.*;

@Entity @Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role() {}
    public Role(RoleType name) { this.name = name; }

    public Long getId() { return id; }
    public RoleType getName() { return name; }
    public void setName(RoleType name) { this.name = name; }
}
