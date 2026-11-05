package com.warehouse.logisticsdelivery.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentCapacity;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentLoad;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DepartmentCapacityBalancerTest {

    private static final DepartmentId KATOWICE = new DepartmentId(1L);

    private static final DepartmentId GLIWICE = new DepartmentId(2L);

    private static final DepartmentId WARSZAWA = new DepartmentId(3L);

    private static final DepartmentId CHORZOW = new DepartmentId(4L);

    private final DepartmentCapacityBalancer balancer = new DepartmentCapacityBalancer();

    @Test
    void shouldUseDepartmentCapacityAndThresholdForLoad() {
        final DepartmentLoad katowice = balancer.load(department(KATOWICE, true, 50.26, 19.02, 10, 80, false), 8);
        final DepartmentLoad gliwice = balancer.load(department(GLIWICE, true, 50.29, 18.67, 100, 95, false), 90);

        assertThat(katowice.dailyCapacity()).isEqualTo(10);
        assertThat(katowice.utilizationPercent()).isEqualTo(80);
        assertThat(katowice.warning()).isTrue();
        assertThat(katowice.overloaded()).isFalse();
        assertThat(gliwice.warning()).isFalse();
    }

    @Test
    void shouldKeepResolvedDepartmentWhenItsRedirectIsDisabled() {
        final List<DepartmentCapacity> departments = departments(false);

        assertThat(balancer.allocate(KATOWICE, departments, loads(Map.of(KATOWICE, 10L)))).isEqualTo(KATOWICE);
    }

    @Test
    void shouldRedirectOverflowToNearestActiveDepartmentWithFreeCapacity() {
        final List<DepartmentCapacity> departments = departments(true);

        assertThat(balancer.allocate(KATOWICE, departments, loads(Map.of(KATOWICE, 10L)))).isEqualTo(GLIWICE);
        assertThat(balancer.allocate(KATOWICE, departments, loads(Map.of(KATOWICE, 10L, GLIWICE, 100L))))
                .isEqualTo(WARSZAWA);
        assertThat(balancer.allocate(KATOWICE, departments.subList(0, 2),
                loads(Map.of(KATOWICE, 10L, GLIWICE, 100L)))).isEqualTo(KATOWICE);
    }

    @Test
    void shouldKeepResolvedDepartmentWithFreeCapacity() {
        assertThat(balancer.allocate(KATOWICE, departments(true), id -> 0L)).isEqualTo(KATOWICE);
    }

    private List<DepartmentCapacity> departments(final boolean redirect) {
        return List.of(
                department(KATOWICE, true, 50.26, 19.02, 10, 85, redirect),
                department(GLIWICE, true, 50.29, 18.67, 100, 85, false),
                department(WARSZAWA, true, 52.23, 21.01, 100, 85, false),
                department(CHORZOW, false, 50.30, 18.95, 100, 85, false));
    }

    private java.util.function.ToLongFunction<DepartmentId> loads(final Map<DepartmentId, Long> loads) {
        return id -> loads.getOrDefault(id, 0L);
    }

    private DepartmentCapacity department(final DepartmentId departmentId, final boolean active,
                                          final double latitude, final double longitude, final int capacity,
                                          final int threshold, final boolean redirect) {
        return new DepartmentCapacity(departmentId, active, latitude, longitude, capacity, threshold, redirect);
    }
}
