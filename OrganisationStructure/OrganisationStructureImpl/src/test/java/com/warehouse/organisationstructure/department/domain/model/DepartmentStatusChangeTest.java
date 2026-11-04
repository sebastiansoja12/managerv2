package com.warehouse.organisationstructure.department.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.exception.DepartmentStatusChangeForbiddenException;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusChange;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepartmentStatusChangeTest {

    private static final UserId USER_ID = new UserId(7L);

    @Test
    void shouldRejectArchivingWhenPrevented() {
        final Department department = department(Department.Status.ACTIVE);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.ARCHIVED,
                change("Closed", new DepartmentStatusPolicy(false, true, false, false, false, 0), List.of())))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("archived");
        assertThat(department.getStatus()).isEqualTo(Department.Status.ACTIVE);
    }

    @Test
    void shouldRejectDeletionWhenPrevented() {
        final Department department = department(Department.Status.ACTIVE);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.DELETED,
                change("Closed", new DepartmentStatusPolicy(true, false, false, false, false, 0), List.of())))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("deleted");
    }

    @Test
    void shouldRequireReasonWhenConfigured() {
        final Department department = department(Department.Status.ACTIVE);
        final DepartmentStatusPolicy policy = new DepartmentStatusPolicy(false, false, true, false, false, 0);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.INACTIVE, change("  ", policy, List.of())))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("requires a reason");

        department.changeStatus(Department.Status.INACTIVE, change(" Renovation ", policy, List.of()));

        assertThat(department.getStatus()).isEqualTo(Department.Status.INACTIVE);
        assertThat(department.getStatusChangeReason()).isEqualTo("Renovation");
        assertThat(department.getStatusChangedAt()).isNotNull();
        assertThat(department.getLastModifiedBy()).isEqualTo(USER_ID);
    }

    @Test
    void shouldBlockDeactivationWithActiveWorkWhenConfigured() {
        final Department department = department(Department.Status.ACTIVE);
        final DepartmentStatusPolicy policy = new DepartmentStatusPolicy(false, false, false, true, false, 0);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.SUSPENDED,
                change(null, policy, List.of("3 active deliveries"))))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("3 active deliveries");
    }

    @Test
    void shouldAllowDeactivationWithActiveWorkWhenNotBlocked() {
        final Department department = department(Department.Status.ACTIVE);

        department.changeStatus(Department.Status.INACTIVE,
                change(null, DepartmentStatusPolicy.unrestricted(), List.of("3 active deliveries")));

        assertThat(department.getStatus()).isEqualTo(Department.Status.INACTIVE);
    }

    @Test
    void shouldRejectActivationWithBlockers() {
        final Department department = department(Department.Status.INACTIVE);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.ACTIVE, new DepartmentStatusChange(
                USER_ID, null, DepartmentStatusPolicy.unrestricted(),
                List.of("No sorting facility connection"), List.of())))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("No sorting facility connection");
        assertThat(department.getStatus()).isEqualTo(Department.Status.INACTIVE);
    }

    @Test
    void shouldNotReviveDeletedDepartment() {
        final Department department = department(Department.Status.DELETED);

        assertThatThrownBy(() -> department.changeStatus(Department.Status.ACTIVE,
                change(null, DepartmentStatusPolicy.unrestricted(), List.of())))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class);
    }

    private DepartmentStatusChange change(final String reason, final DepartmentStatusPolicy policy,
                                          final List<String> activeWork) {
        return new DepartmentStatusChange(USER_ID, reason, policy, List.of(), activeWork);
    }

    private Department department(final Department.Status status) {
        return new Department(new DepartmentId(1L), new DepartmentCode("WA1"), null, null, null, null, null,
                DepartmentType.BRANCH, status, null, null, null, null, null, null, null, null);
    }
}
