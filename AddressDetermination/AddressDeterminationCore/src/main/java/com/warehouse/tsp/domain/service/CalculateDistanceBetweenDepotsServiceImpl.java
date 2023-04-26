package com.warehouse.tsp.domain.service;

import com.warehouse.tsp.domain.model.Depot;

public class CalculateDistanceBetweenDepotsServiceImpl implements CalculateDistanceBetweenDepots {

    private final static Integer EARTH_RADIUS = 6371;
    public static final double PI = 3.14;
    public static final double CIRCUMFERENCE_OF_THE_EQUATOR = 40075.704;
    public static final int ROTATION_OF_EARTH = 360;

    @Override
    public double calculateDistanceBetweenDepots(Depot depot1, Depot depot2) {
        final double lat1 = depot1.getCoordinates().getLat();
        final double lon1 = depot1.getCoordinates().getLon();
        final double lat2 = depot2.getCoordinates().getLat();
        final double lon2 = depot2.getCoordinates().getLon();

        // Haversine formula to calculate distance between two points on Earth
        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLon = Math.toRadians(lon2 - lon1);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    @Override
    public double calculateDistanceBetweenDepotsWithoutEarthCurve(Depot depot1, Depot depot2) {
        final double lat1 = depot1.getCoordinates().getLat();
        final double lon1 = depot1.getCoordinates().getLon();
        final double lat2 = depot2.getCoordinates().getLat();
        final double lon2 = depot2.getCoordinates().getLon();


        final double dLat = lat2 - lat1;
        final double dLon = lon2 - lon1;

        final double result = Math.pow(dLon, 2) + Math.pow(Math.cos((lon2 * PI)/180) * (dLat), 2);

        return (Math.sqrt(result) * CIRCUMFERENCE_OF_THE_EQUATOR) / ROTATION_OF_EARTH;
    }
}
