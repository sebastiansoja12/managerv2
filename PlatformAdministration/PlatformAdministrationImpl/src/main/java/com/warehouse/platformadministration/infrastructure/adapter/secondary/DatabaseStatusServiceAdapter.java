package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.application.port.secondary.DatabaseStatusServicePort;
import com.warehouse.platformadministration.domain.vo.DatabaseStatus;
import com.warehouse.platformadministration.domain.vo.MigrationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
public class DatabaseStatusServiceAdapter implements DatabaseStatusServicePort {

    private static final int VALIDATION_TIMEOUT_SECONDS = 2;

    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    public DatabaseStatusServiceAdapter(final DataSource dataSource, final JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public DatabaseStatus database() {
        final long startedAt = System.nanoTime();
        try (Connection connection = dataSource.getConnection()) {
            final boolean valid = connection.isValid(VALIDATION_TIMEOUT_SECONDS);
            final DatabaseMetaData metaData = connection.getMetaData();
            return new DatabaseStatus(valid, metaData.getDatabaseProductName(), metaData.getDatabaseProductVersion(),
                    elapsedMillis(startedAt));
        } catch (SQLException exception) {
            log.warn("Database status check failed", exception);
            return DatabaseStatus.unavailable(elapsedMillis(startedAt));
        }
    }

    @Override
    public MigrationStatus migrations() {
        try {
            final Long executed = jdbcTemplate.queryForObject("select count(*) from databasechangelog", Long.class);
            final List<MigrationStatus> latest = jdbcTemplate.query(
                    "select id, author, dateexecuted from databasechangelog order by orderexecuted desc limit 1",
                    (resultSet, rowNumber) -> {
                        final Timestamp executedAt = resultSet.getTimestamp("dateexecuted");
                        return new MigrationStatus(executed != null ? executed : 0, resultSet.getString("id"),
                                resultSet.getString("author"), executedAt != null ? executedAt.toInstant() : null);
                    });
            return latest.isEmpty() ? MigrationStatus.unknown() : latest.get(0);
        } catch (DataAccessException exception) {
            log.warn("Liquibase changelog could not be read", exception);
            return MigrationStatus.unknown();
        }
    }

    private long elapsedMillis(final long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
