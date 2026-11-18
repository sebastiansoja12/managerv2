package com.warehouse.organisationstructure.deliveryarea.domain.vo;

import com.warehouse.organisationstructure.deliveryarea.domain.exception.InvalidDeliveryAreaBoundaryException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import java.util.HashSet;
import java.util.List;

public record DeliveryAreaBoundary(List<GeoPoint> points) {

    public static final int MAX_POINTS = 5000;

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    public DeliveryAreaBoundary {
        if (points == null || points.size() < 3 || points.size() > MAX_POINTS) {
            throw new InvalidDeliveryAreaBoundaryException("Boundary must have between 3 and " + MAX_POINTS + " points");
        }
        if (points.stream().anyMatch(point -> point == null || !point.isValid())) {
            throw new InvalidDeliveryAreaBoundaryException("Boundary contains invalid coordinates");
        }
        if (new HashSet<>(points).size() != points.size()) {
            throw new InvalidDeliveryAreaBoundaryException("Boundary contains duplicated points");
        }
        points = List.copyOf(points);
        final Polygon polygon = toPolygon(points, 1);
        if (!polygon.isValid() || polygon.getArea() <= 0) {
            throw new InvalidDeliveryAreaBoundaryException("Boundary must not cross itself");
        }
    }

    public static DeliveryAreaBoundary closedRing(final List<GeoPoint> ring) {
        final boolean closed = ring.size() > 1 && ring.get(0).equals(ring.get(ring.size() - 1));
        return new DeliveryAreaBoundary(closed ? ring.subList(0, ring.size() - 1) : ring);
    }

    public boolean contains(final GeoPoint point) {
        return point != null && point.isValid() && toPolygon(1).covers(
                GEOMETRY_FACTORY.createPoint(new Coordinate(point.longitude(), point.latitude())));
    }

    public double area() {
        return toPolygon(1).getArea();
    }

    public Polygon toPolygon(final double longitudeScale) {
        return toPolygon(points, longitudeScale);
    }

    private static Polygon toPolygon(final List<GeoPoint> points, final double longitudeScale) {
        final Coordinate[] ring = new Coordinate[points.size() + 1];
        for (int index = 0; index < points.size(); index++) {
            ring[index] = new Coordinate(points.get(index).longitude() * longitudeScale, points.get(index).latitude());
        }
        ring[points.size()] = ring[0];
        return GEOMETRY_FACTORY.createPolygon(ring);
    }
}
