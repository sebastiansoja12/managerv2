package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.organisationstructure.deliveryarea.domain.exception.InvalidDeliveryAreaBoundaryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeliveryAreaBoundaryTest {

    @Test
    void shouldAcceptSimplePolygon() {
        final DeliveryAreaBoundary boundary = new DeliveryAreaBoundary(List.of(
                new GeoPoint(52.0, 21.0), new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1)));

        assertThat(boundary.points()).hasSize(3);
    }

    @Test
    void shouldRejectSelfIntersectingPolygon() {
        assertThatThrownBy(() -> new DeliveryAreaBoundary(List.of(
                new GeoPoint(52.0, 21.0), new GeoPoint(52.1, 21.1),
                new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.0))))
                .isInstanceOf(InvalidDeliveryAreaBoundaryException.class);
    }

    @Test
    void shouldRejectTooFewOrInvalidPoints() {
        assertThatThrownBy(() -> new DeliveryAreaBoundary(List.of(new GeoPoint(52.0, 21.0), new GeoPoint(52.1, 21.0))))
                .isInstanceOf(InvalidDeliveryAreaBoundaryException.class);
        assertThatThrownBy(() -> new DeliveryAreaBoundary(List.of(
                new GeoPoint(95.0, 21.0), new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1))))
                .isInstanceOf(InvalidDeliveryAreaBoundaryException.class);
    }

    @Test
    void shouldDropClosingPointOfRing() {
        final GeoPoint first = new GeoPoint(52.0, 21.0);
        final DeliveryAreaBoundary boundary = DeliveryAreaBoundary.closedRing(List.of(
                first, new GeoPoint(52.0, 21.1), new GeoPoint(52.1, 21.1), first));

        assertThat(boundary.points()).hasSize(3);
    }
}
