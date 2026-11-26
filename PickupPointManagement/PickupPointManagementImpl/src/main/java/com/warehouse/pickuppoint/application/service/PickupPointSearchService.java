package com.warehouse.pickuppoint.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointListItemResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchPage;
import com.warehouse.pickuppoint.application.port.secondary.PickupPointSearchRepository;
import com.warehouse.pickuppoint.domain.service.PickupPointDepartmentPolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PickupPointSearchService {

    private final PickupPointSearchRepository pickupPointSearchRepository;
    private final PickupPointDepartmentService pickupPointDepartmentService;

    public PickupPointSearchService(
            final PickupPointSearchRepository pickupPointSearchRepository,
            final PickupPointDepartmentService pickupPointDepartmentService) {
        this.pickupPointSearchRepository = pickupPointSearchRepository;
        this.pickupPointDepartmentService = pickupPointDepartmentService;
    }

    public PickupPointPageResult search(final SearchPickupPointsCommand command, final Instant timestamp) {
        final Map<DepartmentId, PickupPointDepartment> departments =
                this.pickupPointDepartmentService.getCurrentOperatorDepartments();
        final Set<DepartmentId> eligibleDepartmentIds = command.eligibilitySearch()
                ? PickupPointDepartmentPolicy.activeDepartmentIds(departments.values())
                : Set.of();
        final PickupPointSearchPage searchPage = this.pickupPointSearchRepository.search(
                command,
                eligibleDepartmentIds);
        final List<PickupPointListItemResult> items = searchPage.items().stream()
                .map(pickupPoint -> new PickupPointListItemResult(
                        pickupPoint,
                        departments.get(pickupPoint.departmentId()),
                        timestamp))
                .toList();
        return new PickupPointPageResult(
                items,
                command.page(),
                command.size(),
                searchPage.totalElements(),
                searchPage.totalPages(),
                timestamp);
    }
}
