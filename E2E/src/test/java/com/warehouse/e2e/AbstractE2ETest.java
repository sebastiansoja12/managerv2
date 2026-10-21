package com.warehouse.e2e;

import javax.sql.DataSource;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import com.warehouse.Application;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"dev", "e2e"})
public abstract class AbstractE2ETest {

    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:16-alpine");

    @ServiceConnection
    static final ConfluentKafkaContainer KAFKA = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.1");

    static {
        POSTGRESQL.start();
        KAFKA.start();
    }

    protected final TerminalSoapClient terminalSoapClient;

    protected final JdbcClient jdbcClient;

    private final DataSource dataSource;

    protected AbstractE2ETest(final int serverPort, final DataSource dataSource) {
        this.terminalSoapClient = new TerminalSoapClient("http://localhost:" + serverPort + "/v1/api");
        this.jdbcClient = JdbcClient.create(dataSource);
        this.dataSource = dataSource;
    }

    protected void executeSqlScript(final String scriptPath) {
        new ResourceDatabasePopulator(new ClassPathResource(scriptPath)).execute(dataSource);
    }
}
