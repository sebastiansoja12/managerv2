package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

public record MapElementApi(String elementId,
                            String type,
                            String label,
                            Double x,
                            Double y,
                            Double width,
                            Double height,
                            Double rotation,
                            String color,
                            String locationCode,
                            Integer capacity) {
}
