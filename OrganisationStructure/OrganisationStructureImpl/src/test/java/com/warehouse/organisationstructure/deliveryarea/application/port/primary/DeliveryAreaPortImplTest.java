package com.warehouse.organisationstructure.deliveryarea.application.port.primary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.CreateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.GenerateDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.ReplaceDepartmentDeliveryAreasCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.command.UpdateDeliveryAreaCommand;
import com.warehouse.organisationstructure.deliveryarea.application.port.primary.result.DepartmentDeliveryAreas;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaDepartmentServicePort;
import com.warehouse.organisationstructure.deliveryarea.application.port.secondary.DeliveryAreaRepository;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DeliveryAreaDepartmentNotFoundException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DeliveryAreaNotFoundException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.ManualDeliveryAreaEditDisabledException;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.service.DeliveryAreaGenerator;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDraft;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaPolicies;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaRules;
import com.warehouse.commonassets.identificator.DeliveryAreaId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryAreaPortImplTest {

    private static final DepartmentId WARSAW_ID = new DepartmentId(1L);

    private static final DepartmentId KRAKOW_ID = new DepartmentId(2L);

    private static final DeliveryAreaBoundary COUNTRY = new DeliveryAreaBoundary(List.of(
            new GeoPoint(49.0, 14.0), new GeoPoint(49.0, 24.0), new GeoPoint(55.0, 24.0), new GeoPoint(55.0, 14.0)));

    private static final DeliveryAreaBoundary SQUARE = new DeliveryAreaBoundary(List.of(
            new GeoPoint(52.0, 21.0), new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1), new GeoPoint(52.1, 21.0)));

    private static final DeliveryAreaBoundary TRIANGLE = new DeliveryAreaBoundary(List.of(
            new GeoPoint(52.0, 21.0), new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1)));

    private final InMemoryDeliveryAreaRepository repository = new InMemoryDeliveryAreaRepository();

    private final InMemoryDepartments departments = new InMemoryDepartments(Map.of(
            WARSAW_ID, department(WARSAW_ID, "WA1", 52.23, 21.01),
            KRAKOW_ID, department(KRAKOW_ID, "KR1", 50.06, 19.94)));

    private boolean automaticAssignmentEnabled;

    private boolean manualEditAllowed = true;

    private boolean recalculationOnStatusChangeEnabled;

    private final DeliveryAreaPort port = new DeliveryAreaPortImpl(repository, departments,
            () -> DeliveryAreaPolicies.uniform(new DeliveryAreaRules(automaticAssignmentEnabled, manualEditAllowed,
                    recalculationOnStatusChangeEnabled, 0)), new DeliveryAreaGenerator(COUNTRY),
            Clock.fixed(Instant.parse("2026-10-28T10:00:00Z"), ZoneOffset.UTC));

    @Test
    void shouldCreateManualAreaForExistingDepartment() {
        final DeliveryArea created = port.create(new CreateDeliveryAreaCommand(WARSAW_ID,
                new DeliveryAreaName("Centrum"), SQUARE));

        assertThat(created.getDeliveryAreaId()).isNotNull();
        assertThat(created.getSource()).isEqualTo(DeliveryAreaSource.MANUAL);
        assertThat(port.findByDepartment(WARSAW_ID)).hasSize(1);
    }

    @Test
    void shouldRejectUnknownDepartmentAndArea() {
        assertThatThrownBy(() -> port.create(new CreateDeliveryAreaCommand(new DepartmentId(99L),
                new DeliveryAreaName("Centrum"), SQUARE)))
                .isInstanceOf(DeliveryAreaDepartmentNotFoundException.class);
        assertThatThrownBy(() -> port.delete(new DeliveryAreaId(99L)))
                .isInstanceOf(DeliveryAreaNotFoundException.class);
    }

    @Test
    void shouldReplaceDepartmentAreasKeepingUpdatedAndRemovingMissing() {
        final DeliveryArea kept = port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("A"), SQUARE));
        final DeliveryArea removed = port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("B"), SQUARE));

        final List<DeliveryArea> result = port.replaceDepartmentAreas(new ReplaceDepartmentDeliveryAreasCommand(WARSAW_ID,
                List.of(new DeliveryAreaDraft(kept.getDeliveryAreaId(), new DeliveryAreaName("A2"), TRIANGLE),
                        new DeliveryAreaDraft(null, new DeliveryAreaName("C"), SQUARE))));

        assertThat(result).extracting(area -> area.getName().value()).containsExactly("A2", "C");
        assertThat(result.get(0).getDeliveryAreaId()).isEqualTo(kept.getDeliveryAreaId());
        assertThat(repository.findById(removed.getDeliveryAreaId())).isEmpty();
    }

    @Test
    void shouldPreviewWithoutSavingAndApplyReplacingExistingAreas() {
        port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("Manual"), SQUARE));
        final GenerateDeliveryAreasCommand command = new GenerateDeliveryAreasCommand(List.of(WARSAW_ID, KRAKOW_ID));

        final List<DepartmentDeliveryAreas> preview = port.previewAutomatic(command);

        assertThat(preview).hasSize(2);
        assertThat(port.findAll()).hasSize(1);

        final List<DepartmentDeliveryAreas> applied = port.applyAutomatic(command);

        assertThat(applied).extracting(DepartmentDeliveryAreas::departmentId).containsExactly(WARSAW_ID, KRAKOW_ID);
        assertThat(port.findAll()).isNotEmpty()
                .allSatisfy(area -> assertThat(area.getSource()).isEqualTo(DeliveryAreaSource.AUTOMATIC));
    }

    @Test
    void shouldSkipAssignmentAfterDepartmentCreatedWhenDisabled() {
        port.assignAutomaticallyAfterDepartmentCreated(KRAKOW_ID);

        assertThat(port.findAll()).isEmpty();
    }

    @Test
    void shouldReassignAllEligibleDepartmentsAfterDepartmentCreated() {
        automaticAssignmentEnabled = true;
        departments.listed = List.of(departments.byId.get(WARSAW_ID));
        port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("Manual"), SQUARE));

        port.assignAutomaticallyAfterDepartmentCreated(KRAKOW_ID);

        assertThat(port.findByDepartment(WARSAW_ID)).isNotEmpty()
                .allSatisfy(area -> assertThat(area.getSource()).isEqualTo(DeliveryAreaSource.AUTOMATIC));
        assertThat(port.findByDepartment(KRAKOW_ID)).isNotEmpty();
    }

    @Test
    void shouldRedistributeDeletedDepartmentAreasAmongRemainingDepartments() {
        automaticAssignmentEnabled = true;
        final DeliveryAreaDepartment deletedWarsaw = new DeliveryAreaDepartment(WARSAW_ID, new DepartmentCode("WA1"),
                false, CountryCode.PL, new GeoPoint(52.23, 21.01));
        departments.listed = List.of(departments.byId.get(KRAKOW_ID), departments.byId.get(WARSAW_ID));
        port.applyAutomatic(new GenerateDeliveryAreasCommand(List.of(WARSAW_ID, KRAKOW_ID)));
        final double krakowAreaBefore = totalArea(KRAKOW_ID);
        departments.listed = List.of(departments.byId.get(KRAKOW_ID), deletedWarsaw);

        port.reassignAfterDepartmentDeleted(WARSAW_ID);

        assertThat(repository.findByDepartmentId(WARSAW_ID)).isEmpty();
        assertThat(totalArea(KRAKOW_ID)).isGreaterThan(krakowAreaBefore);
        assertThat(port.findServingDepartment(new GeoPoint(52.23, 21.01))).contains(KRAKOW_ID);
    }

    @Test
    void shouldOnlyRemoveDeletedDepartmentAreasWhenAutomaticAssignmentDisabled() {
        port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("Centrum"), SQUARE));
        final DeliveryArea krakowArea = port.create(new CreateDeliveryAreaCommand(KRAKOW_ID,
                new DeliveryAreaName("Manual"), TRIANGLE));

        port.reassignAfterDepartmentDeleted(WARSAW_ID);

        assertThat(port.findAll()).containsExactly(krakowArea);
    }

    @Test
    void shouldFindDepartmentWhoseAreaCoversLocationPreferringSmallestArea() {
        final DeliveryAreaBoundary large = new DeliveryAreaBoundary(List.of(
                new GeoPoint(51.0, 20.0), new GeoPoint(51.0, 22.0), new GeoPoint(53.0, 22.0), new GeoPoint(53.0, 20.0)));
        port.create(new CreateDeliveryAreaCommand(KRAKOW_ID, new DeliveryAreaName("Duży"), large));
        port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("Centrum"), SQUARE));

        assertThat(port.findServingDepartment(new GeoPoint(52.05, 21.05))).contains(WARSAW_ID);
        assertThat(port.findServingDepartment(new GeoPoint(52.5, 20.5))).contains(KRAKOW_ID);
        assertThat(port.findServingDepartment(new GeoPoint(54.0, 18.0))).isEmpty();
    }

    @Test
    void shouldRejectManualChangesWhenManualEditDisabled() {
        final DeliveryArea area = port.create(new CreateDeliveryAreaCommand(WARSAW_ID,
                new DeliveryAreaName("Centrum"), SQUARE));
        manualEditAllowed = false;

        assertThatThrownBy(() -> port.create(new CreateDeliveryAreaCommand(WARSAW_ID,
                new DeliveryAreaName("Nowa"), SQUARE)))
                .isInstanceOf(ManualDeliveryAreaEditDisabledException.class);
        assertThatThrownBy(() -> port.update(new UpdateDeliveryAreaCommand(area.getDeliveryAreaId(),
                new DeliveryAreaName("Zmiana"), TRIANGLE)))
                .isInstanceOf(ManualDeliveryAreaEditDisabledException.class);
        assertThatThrownBy(() -> port.delete(area.getDeliveryAreaId()))
                .isInstanceOf(ManualDeliveryAreaEditDisabledException.class);
        assertThatThrownBy(() -> port.replaceDepartmentAreas(new ReplaceDepartmentDeliveryAreasCommand(WARSAW_ID,
                List.of())))
                .isInstanceOf(ManualDeliveryAreaEditDisabledException.class);
        assertThat(port.findAll()).containsExactly(area);
    }

    @Test
    void shouldKeepAreasAfterStatusChangeWhenRecalculationDisabled() {
        final DeliveryArea area = port.create(new CreateDeliveryAreaCommand(WARSAW_ID,
                new DeliveryAreaName("Centrum"), SQUARE));
        departments.byId.put(WARSAW_ID, inactive(WARSAW_ID, "WA1", 52.23, 21.01));

        port.recalculateAfterDepartmentStatusChanged(WARSAW_ID);

        assertThat(port.findAll()).containsExactly(area);
    }

    @Test
    void shouldRemoveAreasOfInactiveDepartmentWhenRecalculationEnabled() {
        recalculationOnStatusChangeEnabled = true;
        port.create(new CreateDeliveryAreaCommand(WARSAW_ID, new DeliveryAreaName("Centrum"), SQUARE));
        final DeliveryArea krakowArea = port.create(new CreateDeliveryAreaCommand(KRAKOW_ID,
                new DeliveryAreaName("Manual"), TRIANGLE));
        departments.byId.put(WARSAW_ID, inactive(WARSAW_ID, "WA1", 52.23, 21.01));

        port.recalculateAfterDepartmentStatusChanged(WARSAW_ID);

        assertThat(port.findAll()).containsExactly(krakowArea);
    }

    @Test
    void shouldRedistributeAreasAfterStatusChangeWhenRecalculationAndAutomaticAssignmentEnabled() {
        recalculationOnStatusChangeEnabled = true;
        automaticAssignmentEnabled = true;
        departments.listed = List.of(departments.byId.get(KRAKOW_ID), departments.byId.get(WARSAW_ID));
        port.applyAutomatic(new GenerateDeliveryAreasCommand(List.of(WARSAW_ID, KRAKOW_ID)));
        final double krakowAreaBefore = totalArea(KRAKOW_ID);
        final DeliveryAreaDepartment suspendedWarsaw = inactive(WARSAW_ID, "WA1", 52.23, 21.01);
        departments.byId.put(WARSAW_ID, suspendedWarsaw);
        departments.listed = List.of(departments.byId.get(KRAKOW_ID), suspendedWarsaw);

        port.recalculateAfterDepartmentStatusChanged(WARSAW_ID);

        assertThat(repository.findByDepartmentId(WARSAW_ID)).isEmpty();
        assertThat(totalArea(KRAKOW_ID)).isGreaterThan(krakowAreaBefore);
    }

    private static DeliveryAreaDepartment inactive(final DepartmentId departmentId, final String code,
                                                   final double latitude, final double longitude) {
        return new DeliveryAreaDepartment(departmentId, new DepartmentCode(code), false, CountryCode.PL,
                new GeoPoint(latitude, longitude));
    }

    private double totalArea(final DepartmentId departmentId) {
        return repository.findByDepartmentId(departmentId).stream()
                .mapToDouble(area -> area.getBoundary().area())
                .sum();
    }

    private static DeliveryAreaDepartment department(final DepartmentId departmentId, final String code,
                                                     final double latitude, final double longitude) {
        return new DeliveryAreaDepartment(departmentId, new DepartmentCode(code), true, CountryCode.PL,
                new GeoPoint(latitude, longitude));
    }

    private static final class InMemoryDepartments implements DeliveryAreaDepartmentServicePort {

        private final Map<DepartmentId, DeliveryAreaDepartment> byId;

        private List<DeliveryAreaDepartment> listed = List.of();

        private InMemoryDepartments(final Map<DepartmentId, DeliveryAreaDepartment> byId) {
            this.byId = new LinkedHashMap<>(byId);
        }

        @Override
        public Optional<DeliveryAreaDepartment> findDepartment(final DepartmentId departmentId) {
            return Optional.ofNullable(byId.get(departmentId));
        }

        @Override
        public List<DeliveryAreaDepartment> findDepartments() {
            return listed;
        }
    }

    private static final class InMemoryDeliveryAreaRepository implements DeliveryAreaRepository {

        private final Map<DeliveryAreaId, DeliveryArea> areas = new LinkedHashMap<>();

        @Override
        public List<DeliveryArea> findAll() {
            return List.copyOf(areas.values());
        }

        @Override
        public List<DeliveryArea> findByDepartmentId(final DepartmentId departmentId) {
            return areas.values().stream().filter(area -> area.getDepartmentId().equals(departmentId)).toList();
        }

        @Override
        public Optional<DeliveryArea> findById(final DeliveryAreaId deliveryAreaId) {
            return Optional.ofNullable(areas.get(deliveryAreaId));
        }

        @Override
        public DeliveryArea create(final DeliveryArea deliveryArea) {
            areas.put(deliveryArea.getDeliveryAreaId(), deliveryArea);
            return deliveryArea;
        }

        @Override
        public DeliveryArea update(final DeliveryArea deliveryArea) {
            areas.put(deliveryArea.getDeliveryAreaId(), deliveryArea);
            return deliveryArea;
        }

        @Override
        public void delete(final DeliveryAreaId deliveryAreaId) {
            areas.remove(deliveryAreaId);
        }
    }
}
