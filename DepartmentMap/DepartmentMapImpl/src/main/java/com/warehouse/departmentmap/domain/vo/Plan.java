package com.warehouse.departmentmap.domain.vo;

import com.warehouse.departmentmap.domain.model.StorageLocation;

import java.util.List;

public record Plan(List<StorageLocation> created, List<StorageLocation> changed, List<StorageLocation> removed) {
}
