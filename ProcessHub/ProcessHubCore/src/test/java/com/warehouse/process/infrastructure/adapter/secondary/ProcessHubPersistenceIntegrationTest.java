package com.warehouse.process.infrastructure.adapter.secondary;

import static com.warehouse.process.ProcessHubTestFixtures.finishedProcessLog;
import static com.warehouse.process.ProcessHubTestFixtures.processId;
import static com.warehouse.process.ProcessHubTestFixtures.processLog;
import static com.warehouse.process.ProcessHubTestFixtures.shipmentRejected;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.model.ProcessLog;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = ProcessHubPersistenceIntegrationTest.JpaTestConfiguration.class)
class ProcessHubPersistenceIntegrationTest {

    @Autowired
    private ProcessLogReadRepository readRepository;

    @Autowired
    private ProcessLogWriteRepository writeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSyncReadModelWithAllCommunicationLogsUsingRealDatabase() {
        final ProcessLogReadModelRepositoryImpl adapter = new ProcessLogReadModelRepositoryImpl(readRepository);
        adapter.createProcessLog(processLog());
        flushAndClear();

        adapter.syncProcessLog(finishedProcessLog());
        flushAndClear();

        final ProcessLogReadEntity saved = readRepository.findById(processId()).orElseThrow();

        assertThat(saved.getStatus()).isEqualTo(ProcessStatus.SUCCESS);
        assertThat(saved.getCommunicationLogs())
                .hasSize(2)
                .extracting(log -> log.getRequest())
                .containsExactlyInAnyOrder("reject-request", "update-request");
    }

    @Test
    void shouldPersistFinishedWriteModelWithCommunicationLogsUsingRealDatabase() {
        final ProcessRepositoryImpl adapter = new ProcessRepositoryImpl(readRepository, writeRepository);
        final ProcessLog processLog = processLog();
        processLog.applyShipmentRejected(shipmentRejected("reject-request", "reject-response", null));

        adapter.create(processLog);
        processLog.changeStatus(ProcessStatus.SUCCESS);
        adapter.update(processLog);
        flushAndClear();

        final ProcessLogWriteEntity saved = writeRepository.findById(processId()).orElseThrow();

        assertThat(saved.getStatus()).isEqualTo(ProcessStatus.SUCCESS);
        assertThat(saved.getCommunicationLogs())
                .hasSize(1)
                .extracting(log -> log.getRequest())
                .containsExactly("reject-request");
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @EnableJpaRepositories(basePackageClasses = {
            ProcessLogReadRepository.class,
            ProcessLogWriteRepository.class
    })
    @EntityScan(basePackageClasses = {
            ProcessLogReadEntity.class,
            ProcessLogWriteEntity.class
    })
    static class JpaTestConfiguration {
    }
}
