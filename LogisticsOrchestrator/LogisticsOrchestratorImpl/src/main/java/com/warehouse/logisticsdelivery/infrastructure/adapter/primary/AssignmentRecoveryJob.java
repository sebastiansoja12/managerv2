package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPort;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class AssignmentRecoveryJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(AssignmentRecoveryJob.class);

    private final OperatorApiService operatorApiService;

    private final OperatorContext operatorContext;

    private final DepartmentAssignmentPort departmentAssignmentPort;

    public AssignmentRecoveryJob(final OperatorApiService operatorApiService,
                               final OperatorContext operatorContext,
                               final DepartmentAssignmentPort departmentAssignmentPort) {
        this.operatorApiService = operatorApiService;
        this.operatorContext = operatorContext;
        this.departmentAssignmentPort = departmentAssignmentPort;
    }

    @Override
    protected void executeInternal(final JobExecutionContext context) {
        for (final OperatorIdDto operator : this.operatorApiService.findActiveOperators()) {
            final OperatorId operatorId = new OperatorId(operator.value());
            try {
                final int redispatched = this.operatorContext.runAs(operatorId, this.departmentAssignmentPort::recover);
                if (redispatched > 0) {
                    log.warn("Re-dispatched {} department assignment runs of operator {}", redispatched,
                            operator.value());
                }
            } catch (final RuntimeException exception) {
                log.warn("Courier assignment recovery failed for operator {}: {}", operator.value(),
                        exception.getMessage());
            }
        }
    }
}
