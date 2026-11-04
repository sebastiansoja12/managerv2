package com.warehouse.organisationstructure.deliveryarea.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DepartmentNotEligibleForAutomaticAreasException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DuplicateDepartmentCoordinatesException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.NoDepartmentsSelectedException;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

class DeliveryAreaGeneratorTest {

    private static final Instant NOW = Instant.parse("2026-10-28T10:00:00Z");

    private static final DeliveryAreaBoundary COUNTRY = new DeliveryAreaBoundary(List.of(
            new GeoPoint(49.0, 14.0), new GeoPoint(49.0, 24.0), new GeoPoint(55.0, 24.0), new GeoPoint(55.0, 14.0)));

    private final DeliveryAreaGenerator generator = new DeliveryAreaGenerator(COUNTRY);

    private final DeliveryAreaDepartment warsaw = department(1L, "WA1", 52.23, 21.01);

    private final DeliveryAreaDepartment krakow = department(2L, "KR1", 50.06, 19.94);

    @Test
    void shouldSplitCountryBetweenDepartments() {
        final Map<DeliveryAreaDepartment, List<DeliveryArea>> result = generator.generate(List.of(warsaw, krakow), NOW);

        assertThat(result).containsOnlyKeys(warsaw, krakow);
        assertThat(result.get(warsaw)).hasSize(1);
        final DeliveryArea area = result.get(warsaw).get(0);
        assertThat(area.getDepartmentId()).isEqualTo(new DepartmentId(1L));
        assertThat(area.getSource()).isEqualTo(DeliveryAreaSource.AUTOMATIC);
        assertThat(area.getName().value()).isEqualTo("WA1 · 1");
        assertThat(area.getBoundary().toPolygon(1).contains(point(52.23, 21.01))).isTrue();
        assertThat(area.getBoundary().toPolygon(1).contains(point(50.06, 19.94))).isFalse();
        final double coveredArea = result.values().stream().flatMap(List::stream)
                .mapToDouble(generated -> generated.getBoundary().toPolygon(1).getArea())
                .sum();
        assertThat(coveredArea).isCloseTo(COUNTRY.toPolygon(1).getArea(), offset(1e-6));
    }

    @Test
    void shouldGiveWholeCountryToSingleDepartment() {
        final List<DeliveryArea> areas = generator.generate(List.of(warsaw), NOW).get(warsaw);

        assertThat(areas).hasSize(1);
        assertThat(areas.get(0).getBoundary().toPolygon(1).getArea())
                .isCloseTo(COUNTRY.toPolygon(1).getArea(), offset(1e-6));
    }

    @Test
    void shouldLimitAreasToConfiguredRadiusInRadiusMode() {
        final DeliveryArea area = generator.generate(List.of(warsaw, krakow), NOW, department -> department == warsaw ? 50 : 0).get(warsaw).get(0);
        final Polygon polygon = area.getBoundary().toPolygon(1);

        assertThat(polygon.contains(point(52.23, 21.01))).isTrue();
        assertThat(polygon.contains(point(52.50, 21.01))).isTrue();
        assertThat(polygon.contains(point(52.80, 21.01))).isFalse();
        assertThat(polygon.contains(point(52.23, 22.0))).isFalse();
    }

    @Test
    void shouldRejectInvalidSelection() {
        assertThatThrownBy(() -> generator.generate(List.of(), NOW))
                .isInstanceOf(NoDepartmentsSelectedException.class);
        assertThatThrownBy(() -> generator.generate(List.of(warsaw, department(3L, "WA2", 52.23, 21.01)), NOW))
                .isInstanceOf(DuplicateDepartmentCoordinatesException.class);
        assertThatThrownBy(() -> generator.generate(List.of(department(4L, "BER", 52.52, 13.40)), NOW))
                .isInstanceOf(DepartmentNotEligibleForAutomaticAreasException.class);
        assertThatThrownBy(() -> generator.generate(List.of(new DeliveryAreaDepartment(new DepartmentId(5L),
                new DepartmentCode("IN1"), false, CountryCode.PL, new GeoPoint(51.0, 20.0))), NOW))
                .isInstanceOf(DepartmentNotEligibleForAutomaticAreasException.class);
    }

    private static DeliveryAreaDepartment department(final Long id, final String code,
                                                     final double latitude, final double longitude) {
        return new DeliveryAreaDepartment(new DepartmentId(id), new DepartmentCode(code), true, CountryCode.PL,
                new GeoPoint(latitude, longitude));
    }

    private static Point point(final double latitude, final double longitude) {
        return new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
    }
}
