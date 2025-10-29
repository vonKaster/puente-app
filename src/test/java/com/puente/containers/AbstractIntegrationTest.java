package com.puente.containers;

import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractIntegrationTest {
    static final PostgresTestContainer container = PostgresTestContainer.getInstance();

    @BeforeAll
    static void init() { container.start(); }
}