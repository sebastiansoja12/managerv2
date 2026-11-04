package com.warehouse.organisationstructure.department.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class InactiveDepartmentArchiveJob {

    private final DepartmentPort departmentPort;

    private final OperatorApiService operatorApiService;

    private final OperatorContext operatorContext;

    public InactiveDepartmentArchiveJob(final DepartmentPort departmentPort,
                                        final OperatorApiService operatorApiService,
                                        final OperatorContext operatorContext) {
        this.departmentPort = departmentPort;
        this.operatorApiService = operatorApiService;
        this.operatorContext = operatorContext;
    }

    @Scheduled(cron = "${manager.department.auto-archive.cron:0 30 2 * * *}")
    public void archiveInactiveDepartments() {
        for (final OperatorIdDto operatorId : this.operatorApiService.findActiveOperators()) {
            try {
                final int archived = this.operatorContext.runAs(new OperatorId(operatorId.value()),
                        this.departmentPort::archiveInactiveDepartments);
                if (archived > 0) {
                    log.info("Archived {} inactive departments of operator {}", archived, operatorId.value());
                }
            } catch (final RuntimeException exception) {
                log.warn("Inactive departments of operator {} were not archived: {}", operatorId.value(),
                        exception.getMessage());
            }
        }
    }
}
