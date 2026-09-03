package com.warehouse.platformadministration.domain.vo;

import java.time.Instant;
import java.util.List;

public record RuntimeStatus(String applicationName,
                            String version,
                            String javaVersion,
                            Instant startedAt,
                            long uptimeSeconds,
                            List<String> activeProfiles,
                            long usedMemoryMegabytes,
                            long maxMemoryMegabytes,
                            int availableProcessors) {
}
