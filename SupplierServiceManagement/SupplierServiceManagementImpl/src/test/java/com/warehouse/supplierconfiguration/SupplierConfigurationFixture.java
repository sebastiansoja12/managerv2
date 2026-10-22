package com.warehouse.supplierconfiguration;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplierconfiguration.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationHistoryRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationPolicyRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierConfigurationRepository;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierDirectoryServicePort;
import com.warehouse.supplierconfiguration.application.port.secondary.SupplierProfile;
import com.warehouse.supplierconfiguration.domain.enumeration.SupplierSetting;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfiguration;
import com.warehouse.supplierconfiguration.domain.model.SupplierConfigurationPolicy;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationPolicyId;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationRevision;
import com.warehouse.supplierconfiguration.domain.vo.SupplierConfigurationSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class SupplierConfigurationFixture {

    public static final DepartmentId KATOWICE = new DepartmentId(1L);
    public static final DepartmentId KRAKOW = new DepartmentId(2L);

    public static final SupplierProfile FIRST_SUPPLIER = new SupplierProfile(new SupplierId(11L),
            new SupplierCode("KUR-01"), "Adam", "Nowak", KATOWICE, true);
    public static final SupplierProfile SECOND_SUPPLIER = new SupplierProfile(new SupplierId(12L),
            new SupplierCode("KUR-02"), "Ewa", "Kowalska", KATOWICE, true);
    public static final SupplierProfile INACTIVE_SUPPLIER = new SupplierProfile(new SupplierId(13L),
            new SupplierCode("KUR-03"), "Jan", "Zielinski", KATOWICE, false);
    public static final SupplierProfile OTHER_DEPARTMENT_SUPPLIER = new SupplierProfile(new SupplierId(14L),
            new SupplierCode("KUR-04"), "Anna", "Mazur", KRAKOW, true);

    public static final UserId FIRST_SUPPLIER_USER = new UserId(101L);
    public static final UserId SECOND_SUPPLIER_USER = new UserId(102L);
    public static final UserId ADMIN_USER = new UserId(5001L);

    private SupplierConfigurationFixture() {
    }

    public static SupplierConfigurationPolicy policy(final Map<SupplierSetting, Boolean> enforcedSettings) {
        return new SupplierConfigurationPolicy(SupplierConfigurationPolicyId.generate(), enforcedSettings, 0L, null, null);
    }

    public static final class InMemorySupplierConfigurationRepository implements SupplierConfigurationRepository {

        private final Map<SupplierId, SupplierConfigurationSnapshot> snapshots = new HashMap<>();

        @Override
        public Optional<SupplierConfiguration> findBySupplierId(final SupplierId supplierId) {
            return Optional.ofNullable(snapshots.get(supplierId)).map(SupplierConfiguration::restore);
        }

        @Override
        public SupplierConfiguration save(final SupplierConfiguration supplierConfiguration) {
            final SupplierConfigurationSnapshot snapshot = supplierConfiguration.snapshot();
            final long nextVersion = snapshot.version() == null ? 0L : snapshot.version() + 1;
            final SupplierConfigurationSnapshot savedSnapshot = new SupplierConfigurationSnapshot(snapshot.supplierId(),
                    snapshot.values(), nextVersion, snapshot.createdAt(), snapshot.updatedAt(), snapshot.updatedBy());
            snapshots.put(snapshot.supplierId(), savedSnapshot);
            return SupplierConfiguration.restore(savedSnapshot);
        }
    }

    public static final class InMemorySupplierConfigurationPolicyRepository
            implements SupplierConfigurationPolicyRepository {

        private SupplierConfigurationPolicy policy;

        public void store(final SupplierConfigurationPolicy policy) {
            this.policy = policy;
        }

        @Override
        public Optional<SupplierConfigurationPolicy> findCurrent() {
            return Optional.ofNullable(policy);
        }

        @Override
        public SupplierConfigurationPolicy save(final SupplierConfigurationPolicy policy) {
            final long nextVersion = policy.getVersion() == null ? 0L : policy.getVersion() + 1;
            this.policy = new SupplierConfigurationPolicy(policy.getPolicyId(), policy.getEnforcedSettings(),
                    nextVersion, policy.getUpdatedAt(), policy.getUpdatedBy());
            return this.policy;
        }
    }

    public static final class InMemorySupplierConfigurationHistoryRepository
            implements SupplierConfigurationHistoryRepository {

        private final List<SupplierConfigurationRevision> revisions = new ArrayList<>();

        @Override
        public void append(final SupplierConfigurationRevision revision) {
            revisions.add(revision);
        }

        @Override
        public List<SupplierConfigurationRevision> findBySupplierId(final SupplierId supplierId) {
            return revisions.stream()
                    .filter(revision -> revision.supplierId().equals(supplierId))
                    .toList();
        }
    }

    public static final class InMemorySupplierDirectory implements SupplierDirectoryServicePort {

        private final Map<UserId, SupplierProfile> suppliersByUser = Map.of(
                FIRST_SUPPLIER_USER, FIRST_SUPPLIER,
                SECOND_SUPPLIER_USER, SECOND_SUPPLIER);
        private final List<SupplierProfile> suppliers = List.of(FIRST_SUPPLIER, SECOND_SUPPLIER, INACTIVE_SUPPLIER,
                OTHER_DEPARTMENT_SUPPLIER);

        @Override
        public Optional<SupplierProfile> findBySupplierId(final SupplierId supplierId) {
            return suppliers.stream().filter(supplier -> supplier.supplierId().equals(supplierId)).findFirst();
        }

        @Override
        public Optional<SupplierProfile> findBySupplierCode(final SupplierCode supplierCode) {
            return suppliers.stream().filter(supplier -> supplier.supplierCode().equals(supplierCode)).findFirst();
        }

        @Override
        public Optional<SupplierProfile> findByUserId(final UserId userId) {
            return Optional.ofNullable(suppliersByUser.get(userId));
        }

        @Override
        public List<SupplierProfile> findActiveSuppliers() {
            return suppliers.stream().filter(SupplierProfile::active).toList();
        }
    }

    public static final class FakeCurrentUser implements CurrentUserServicePort {

        private UserId currentUserId = FIRST_SUPPLIER_USER;
        private final Set<DepartmentId> accessibleDepartments = new HashSet<>(Set.of(KATOWICE));

        public void signInAs(final UserId userId) {
            this.currentUserId = userId;
        }

        @Override
        public UserId getCurrentUserId() {
            return currentUserId;
        }

        @Override
        public boolean canAccessDepartment(final DepartmentId departmentId) {
            return accessibleDepartments.contains(departmentId);
        }
    }
}
