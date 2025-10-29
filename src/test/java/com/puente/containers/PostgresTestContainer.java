package com.puente.containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {
    private static final String IMAGE = "postgres:16-alpine";
    private static PostgresTestContainer container;

    private PostgresTestContainer() { super(IMAGE); }

    @SuppressWarnings("resource")
    public static PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer()
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        System.setProperty("spring.jpa.hibernate.ddl-auto", "validate");
        System.setProperty("spring.flyway.enabled", "true");
    }

    @Override
    public void stop() {
        // Automático cuando JVM stoppea el container
    }
}