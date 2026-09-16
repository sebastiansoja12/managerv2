package com.warehouse.organisationstructure.deliveryarea.domain.service;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.organisationstructure.deliveryarea.domain.enumeration.DeliveryAreaSource;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DeliveryAreaGenerationException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DepartmentNotEligibleForAutomaticAreasException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.DuplicateDepartmentCoordinatesException;
import com.warehouse.organisationstructure.deliveryarea.domain.exception.NoDepartmentsSelectedException;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.GeoPoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeliveryAreaGenerator {

    private static final CountryCode SUPPORTED_COUNTRY = CountryCode.PL;

    private static final double LONGITUDE_SCALE = Math.cos(Math.toRadians(52));

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final Polygon countryBoundary;

    private final Polygon scaledCountryBoundary;

    public DeliveryAreaGenerator(final DeliveryAreaBoundary countryBoundary) {
        this.countryBoundary = countryBoundary.toPolygon(1);
        this.scaledCountryBoundary = countryBoundary.toPolygon(LONGITUDE_SCALE);
    }

    public boolean isEligible(final DeliveryAreaDepartment department) {
        final GeoPoint location = department.location();
        return department.active() && SUPPORTED_COUNTRY == department.countryCode()
                && location != null && location.isValid()
                && countryBoundary.contains(geometryFactory.createPoint(
                        new Coordinate(location.longitude(), location.latitude())));
    }

    public Map<DeliveryAreaDepartment, List<DeliveryArea>> generate(final List<DeliveryAreaDepartment> departments,
                                                                    final Instant now) {
        if (departments.isEmpty()) {
            throw new NoDepartmentsSelectedException();
        }
        final List<Coordinate> sites = new ArrayList<>();
        final Set<Coordinate> uniqueSites = new HashSet<>();
        for (final DeliveryAreaDepartment department : departments) {
            if (!isEligible(department)) {
                throw new DepartmentNotEligibleForAutomaticAreasException(department.departmentCode());
            }
            final Coordinate site = new Coordinate(department.location().longitude() * LONGITUDE_SCALE,
                    department.location().latitude());
            if (!uniqueSites.add(site)) {
                throw new DuplicateDepartmentCoordinatesException(department.departmentCode());
            }
            sites.add(site);
        }

        final List<Geometry> cells = cells(sites);
        final Map<DeliveryAreaDepartment, List<DeliveryArea>> result = new LinkedHashMap<>();
        for (int index = 0; index < departments.size(); index++) {
            final DeliveryAreaDepartment department = departments.get(index);
            final Geometry clipped = cells.get(index).intersection(scaledCountryBoundary);
            final List<DeliveryArea> areas = new ArrayList<>();
            for (int part = 0; part < clipped.getNumGeometries(); part++) {
                if (clipped.getGeometryN(part) instanceof Polygon polygon && !polygon.isEmpty()) {
                    areas.add(new DeliveryArea(department.departmentId(),
                            new DeliveryAreaName(department.departmentCode().getValue() + " · " + (areas.size() + 1)),
                            unscale(polygon), DeliveryAreaSource.AUTOMATIC, now));
                }
            }
            if (areas.isEmpty()) {
                throw new DeliveryAreaGenerationException("Department " + department.departmentCode().getValue()
                        + " did not receive any area");
            }
            result.put(department, areas);
        }
        return result;
    }

    private List<Geometry> cells(final List<Coordinate> sites) {
        final Envelope clip = new Envelope(scaledCountryBoundary.getEnvelopeInternal());
        clip.expandBy(1);
        if (sites.size() == 1) {
            return List.of(geometryFactory.toGeometry(clip));
        }
        final VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setSites(sites);
        builder.setClipEnvelope(clip);
        final Geometry diagram = builder.getDiagram(geometryFactory);
        final Map<Coordinate, Geometry> cellsBySite = new LinkedHashMap<>();
        for (int index = 0; index < diagram.getNumGeometries(); index++) {
            final Geometry cell = diagram.getGeometryN(index);
            cellsBySite.put((Coordinate) cell.getUserData(), cell);
        }
        return sites.stream().map(site -> {
            final Geometry cell = cellsBySite.get(site);
            if (cell == null) {
                throw new DeliveryAreaGenerationException("Unable to build delivery area cell");
            }
            return cell;
        }).toList();
    }

    private DeliveryAreaBoundary unscale(final Polygon polygon) {
        final Coordinate[] ring = polygon.getExteriorRing().getCoordinates();
        return DeliveryAreaBoundary.closedRing(Arrays.stream(ring)
                .map(coordinate -> new GeoPoint(coordinate.y, coordinate.x / LONGITUDE_SCALE))
                .toList());
    }
}
