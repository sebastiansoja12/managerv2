package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.FindDepartmentCostsCommand;
import com.warehouse.analytics.application.port.primary.command.RecordDepartmentCostCommand;
import com.warehouse.analytics.application.port.secondary.AnalyticsUserServicePort;
import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.service.DepartmentCostFactory;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

public class DepartmentCostPortImpl implements DepartmentCostPort {

    private final DepartmentCostRepository departmentCostRepository;

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    private final AnalyticsUserServicePort analyticsUserServicePort;

    private final DepartmentCostFactory departmentCostFactory;

    public DepartmentCostPortImpl(final DepartmentCostRepository departmentCostRepository,
                                  final DepartmentDirectoryServicePort departmentDirectoryServicePort,
                                  final AnalyticsUserServicePort analyticsUserServicePort,
                                  final DepartmentCostFactory departmentCostFactory) {
        this.departmentCostRepository = departmentCostRepository;
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.analyticsUserServicePort = analyticsUserServicePort;
        this.departmentCostFactory = departmentCostFactory;
    }

    @Override
    @Transactional
    public DepartmentCostId record(final RecordDepartmentCostCommand command) {
        final boolean departmentExists = this.departmentDirectoryServicePort.departments().stream()
                .map(AnalyticsDepartment::departmentCode)
                .anyMatch(command.departmentCode()::equals);
        if (!departmentExists) {
            throw new AnalyticsException(AnalyticsException.Reason.NOT_FOUND,
                    "Department " + command.departmentCode().value() + " does not exist");
        }
        final DepartmentCost departmentCost = this.departmentCostFactory.create(command.departmentCode(),
                command.category(), command.amount(), command.currency(), command.incurredOn(), command.description(),
                this.analyticsUserServicePort.currentUserId());
        this.departmentCostRepository.create(departmentCost);
        return departmentCost.costId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentCost> find(final FindDepartmentCostsCommand command) {
        return this.departmentCostRepository.find(command.period(), command.departmentCode()).stream()
                .sorted(Comparator.comparing(DepartmentCost::incurredOn).reversed()
                        .thenComparing(DepartmentCost::recordedAt, Comparator.reverseOrder()))
                .toList();
    }

    @Override
    @Transactional
    public void delete(final DepartmentCostId costId) {
        final DepartmentCost departmentCost = this.departmentCostRepository.findById(costId)
                .orElseThrow(() -> new AnalyticsException(AnalyticsException.Reason.NOT_FOUND,
                        "Department cost " + costId.value() + " does not exist"));
        this.departmentCostRepository.delete(departmentCost.costId());
    }
}
