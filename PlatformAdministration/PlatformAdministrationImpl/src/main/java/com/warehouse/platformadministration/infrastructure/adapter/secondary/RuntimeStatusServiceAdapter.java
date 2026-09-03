package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.application.port.secondary.RuntimeStatusServicePort;
import com.warehouse.platformadministration.domain.vo.RuntimeStatus;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class RuntimeStatusServiceAdapter implements RuntimeStatusServicePort {

    private static final long MEGABYTE = 1024L * 1024L;

    private final Environment environment;

    private final ObjectProvider<BuildProperties> buildProperties;

    private final Clock clock;

    public RuntimeStatusServiceAdapter(final Environment environment,
                                       final ObjectProvider<BuildProperties> buildProperties,
                                       final Clock clock) {
        this.environment = environment;
        this.buildProperties = buildProperties;
        this.clock = clock;
    }

    @Override
    public RuntimeStatus current() {
        final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        final Instant startedAt = Instant.ofEpochMilli(runtime.getStartTime());
        final Runtime jvm = Runtime.getRuntime();
        return new RuntimeStatus(
                environment.getProperty("spring.application.name", "managerv2"),
                version(),
                System.getProperty("java.version"),
                startedAt,
                Duration.between(startedAt, Instant.now(clock)).toSeconds(),
                Arrays.asList(environment.getActiveProfiles()),
                (jvm.totalMemory() - jvm.freeMemory()) / MEGABYTE,
                jvm.maxMemory() / MEGABYTE,
                jvm.availableProcessors());
    }

    private String version() {
        final BuildProperties properties = buildProperties.getIfAvailable();
        if (properties != null && properties.getVersion() != null) {
            return properties.getVersion();
        }
        final String implementationVersion = getClass().getPackage().getImplementationVersion();
        return implementationVersion != null ? implementationVersion : "dev";
    }
}
