package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentLifecycleServicePort;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentRepository;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.event.DepartmentEvent;
import com.warehouse.organisationstructure.department.domain.exception.DepartmentStatusChangeForbiddenException;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.domain.registry.DomainRegistry;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;
import com.warehouse.organisationstructure.department.domain.vo.TaxId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DepartmentServiceImplTest {

    private static final UserId USER_ID = new UserId(5L);

    private final InMemoryDepartments departments = new InMemoryDepartments();

    private final RecordingLifecycle lifecycle = new RecordingLifecycle();

    private DepartmentStatusPolicy policy = DepartmentStatusPolicy.unrestricted();

    private final DepartmentService departmentService = new DepartmentServiceImpl(departments, departmentId -> policy,
            lifecycle, new WorkTransferTargetSelector());

    @BeforeAll
    static void initializeEventPublisher() {
        new DomainRegistry().setApplicationEventPublisher(event -> {
        });
    }

    @Test
    void shouldCreateDepartmentInactiveWhenActivationRequirementsAreMissing() {
        lifecycle.activationBlockers.add("no direct connection to an active sorting facility");
        final Department department = department(1L, "KT1", Department.Status.ACTIVE, 50.26, 19.02);

        departmentService.createDepartment(department);

        assertThat(lifecycle.created).containsExactly(department.getDepartmentId());
        assertThat(departments.byCode("KT1").getStatus()).isEqualTo(Department.Status.INACTIVE);
        assertThat(departments.byCode("KT1").getStatusChangeReason())
                .contains("no direct connection to an active sorting facility");
    }

    @Test
    void shouldTransferWorkToNearestActiveDepartmentBeforeDeactivation() {
        policy = new DepartmentStatusPolicy(false, false, false, true, true, 0);
        departments.save(department(1L, "KT1", Department.Status.ACTIVE, 50.26, 19.02));
        departments.save(department(2L, "GL1", Department.Status.ACTIVE, 50.29, 18.67));
        departments.save(department(3L, "WA1", Department.Status.ACTIVE, 52.23, 21.01));
        departments.save(department(4L, "CH1", Department.Status.INACTIVE, 50.30, 18.95));
        lifecycle.activeWork.add("2 active deliveries");

        departmentService.deactivateDepartment(new DepartmentCode("KT1"), USER_ID, "Renovation");

        assertThat(lifecycle.transfers).containsExactly(List.of(new DepartmentId(1L), new DepartmentId(2L)));
        assertThat(departments.byCode("KT1").getStatus()).isEqualTo(Department.Status.INACTIVE);
    }

    @Test
    void shouldBlockDeactivationWhenWorkRemainsAndTransferIsManual() {
        policy = new DepartmentStatusPolicy(false, false, false, true, false, 0);
        departments.save(department(1L, "KT1", Department.Status.ACTIVE, 50.26, 19.02));
        lifecycle.activeWork.add("3 assigned couriers");

        assertThatThrownBy(() -> departmentService.changeStatus(new DepartmentCode("KT1"),
                Department.Status.SUSPENDED, null, USER_ID))
                .isInstanceOf(DepartmentStatusChangeForbiddenException.class)
                .hasMessageContaining("3 assigned couriers");
        assertThat(lifecycle.transfers).isEmpty();
    }

    @Test
    void shouldArchiveDepartmentsInactiveLongerThanConfiguredDays() {
        policy = new DepartmentStatusPolicy(false, false, true, false, false, 30);
        final Department expired = department(1L, "KT1", Department.Status.INACTIVE, 50.26, 19.02);
        expired.setUpdatedAt(Instant.now().minus(Duration.ofDays(31)));
        final Department recent = department(2L, "GL1", Department.Status.INACTIVE, 50.29, 18.67);
        recent.setUpdatedAt(Instant.now().minus(Duration.ofDays(2)));
        departments.save(expired);
        departments.save(recent);
        departments.save(department(3L, "WA1", Department.Status.ACTIVE, 52.23, 21.01));

        final int archived = departmentService.archiveInactiveDepartments();

        assertThat(archived).isEqualTo(1);
        assertThat(departments.byCode("KT1").getStatus()).isEqualTo(Department.Status.ARCHIVED);
        assertThat(departments.byCode("KT1").getStatusChangeReason()).contains("30 days");
        assertThat(departments.byCode("GL1").getStatus()).isEqualTo(Department.Status.INACTIVE);
    }

    @Test
    void shouldNotArchiveAutomaticallyWhenDisabledOrArchivingPrevented() {
        final Department expired = department(1L, "KT1", Department.Status.INACTIVE, 50.26, 19.02);
        expired.setUpdatedAt(Instant.now().minus(Duration.ofDays(400)));
        departments.save(expired);

        assertThat(departmentService.archiveInactiveDepartments()).isZero();
        policy = new DepartmentStatusPolicy(false, true, false, false, false, 30);
        assertThat(departmentService.archiveInactiveDepartments()).isZero();
        assertThat(departments.byCode("KT1").getStatus()).isEqualTo(Department.Status.INACTIVE);
    }

    private static Department department(final Long id, final String code, final Department.Status status,
                                         final double latitude, final double longitude) {
        return new Department(new DepartmentId(id), new DepartmentCode(code),
                new Address("Miasto", "Ulica 1", "40-001", CountryCode.PL), new TaxId("1234567890"), "123456789",
                "08:00-16:00", code.toLowerCase() + "@example.com", DepartmentType.BRANCH, status,
                new Coordinates(latitude, longitude), Instant.now(), Instant.now(), null, USER_ID, null, null, null);
    }

    private static final class RecordingLifecycle implements DepartmentLifecycleServicePort {

        private final List<DepartmentId> created = new ArrayList<>();

        private final List<String> activationBlockers = new ArrayList<>();

        private final List<String> activeWork = new ArrayList<>();

        private final List<List<DepartmentId>> transfers = new ArrayList<>();

        @Override
        public void notifyCreated(final DepartmentId departmentId) {
            created.add(departmentId);
        }

        @Override
        public List<String> activationBlockers(final DepartmentId departmentId) {
            return List.copyOf(activationBlockers);
        }

        @Override
        public List<String> activeWork(final DepartmentId departmentId) {
            return transfers.isEmpty() ? List.copyOf(activeWork) : List.of();
        }

        @Override
        public void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
            transfers.add(List.of(sourceDepartmentId, targetDepartmentId));
        }
    }

    private static final class InMemoryDepartments implements DepartmentRepository {

        private final Map<String, Department> departments = new LinkedHashMap<>();

        private void save(final Department department) {
            departments.put(department.getDepartmentCode().getValue(), department);
        }

        private Department byCode(final String code) {
            return departments.get(code);
        }

        @Override
        public void createOrUpdate(final Department department) {
            save(department);
        }

        @Override
        public Department findByDepartmentCode(final DepartmentCode departmentCode) {
            return departments.get(departmentCode.getValue());
        }

        @Override
        public Department findByDepartmentCodeIncludingArchived(final DepartmentCode departmentCode) {
            return departments.get(departmentCode.getValue());
        }

        @Override
        public Department findByDepartmentId(final DepartmentId departmentId) {
            return departments.values().stream()
                    .filter(department -> department.getDepartmentId().equals(departmentId))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Optional<DepartmentCode> findDepartmentCode(final DepartmentId departmentId) {
            return Optional.ofNullable(findByDepartmentId(departmentId)).map(Department::getDepartmentCode);
        }

        @Override
        public List<Department> findAll() {
            return List.copyOf(departments.values());
        }

        @Override
        public List<Department> findAllArchived() {
            return List.of();
        }

        @Override
        public Boolean checkExists(final DepartmentCode departmentCode) {
            return departments.containsKey(departmentCode.getValue());
        }

        @Override
        public boolean checkExistsAnyWithCountryCode(final CountryCode countryCode) {
            return !departments.isEmpty();
        }
    }
}
