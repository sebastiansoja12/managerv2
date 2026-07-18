package com.warehouse.department.infrastructure.adapter.primary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.department.domain.service.DepartmentSyncService;

@RestController
@RequestMapping("/departments/read-sync")
public class DepartmentReadSyncController {

    private final DepartmentSyncService departmentSyncService;

    private final OperatorContext operatorContext;

    public DepartmentReadSyncController(final DepartmentSyncService departmentSyncService,
                                        final OperatorContext operatorContext) {
        this.departmentSyncService = departmentSyncService;
        this.operatorContext = operatorContext;
    }

    @PostMapping("/{operatorId}")
    public ResponseEntity<DepartmentReadSyncResponse> sync(@PathVariable final Long operatorId) {
        return operatorContext.runAs(OperatorId.of(operatorId), () -> {
            final int syncedDepartments = departmentSyncService.syncReadModels();
            return ResponseEntity.ok(new DepartmentReadSyncResponse(operatorId, syncedDepartments));
        });
    }

    public record DepartmentReadSyncResponse(Long operatorId, int syncedDepartments) {
    }
}
