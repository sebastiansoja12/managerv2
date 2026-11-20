package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.FindDepartmentCostsCommand;
import com.warehouse.analytics.application.port.primary.command.RecordDepartmentCostCommand;
import com.warehouse.analytics.application.port.secondary.AnalyticsUserServicePort;
import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.service.DepartmentCostFactory;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static com.warehouse.analytics.AnalyticsFixture.KATOWICE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_CODE;
import static com.warehouse.analytics.AnalyticsFixture.MANAGER;
import static com.warehouse.analytics.AnalyticsFixture.NOVEMBER;
import static com.warehouse.analytics.AnalyticsFixture.cost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DepartmentCostPortImplTest {

    private static final Instant NOW = Instant.parse("2026-11-17T10:00:00Z");

    private final DepartmentCostRepository departmentCostRepository = mock(DepartmentCostRepository.class);

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort =
            mock(DepartmentDirectoryServicePort.class);

    private final AnalyticsUserServicePort analyticsUserServicePort = mock(AnalyticsUserServicePort.class);

    private DepartmentCostPortImpl port;

    @BeforeEach
    void setUp() {
        this.port = new DepartmentCostPortImpl(departmentCostRepository, departmentDirectoryServicePort,
                analyticsUserServicePort, new DepartmentCostFactory(Clock.fixed(NOW, ZoneOffset.UTC)));
        when(departmentDirectoryServicePort.departments()).thenReturn(List.of(KATOWICE));
        when(analyticsUserServicePort.currentUserId()).thenReturn(MANAGER);
    }

    @Test
    void shouldRecordCostOnBehalfOfCurrentUser() {
        final DepartmentCostId costId = port.record(command(KATOWICE_CODE));

        final ArgumentCaptor<DepartmentCost> captor = ArgumentCaptor.forClass(DepartmentCost.class);
        verify(departmentCostRepository).create(captor.capture());
        assertThat(captor.getValue().costId()).isEqualTo(costId);
        assertThat(captor.getValue().recordedBy()).isEqualTo(MANAGER);
        assertThat(captor.getValue().recordedAt()).isEqualTo(NOW);
        assertThat(captor.getValue().amount()).isEqualByComparingTo("320.40");
    }

    @Test
    void shouldRejectCostForUnknownDepartment() {
        assertThatThrownBy(() -> port.record(command(new DepartmentCode("XXX"))))
                .isInstanceOfSatisfying(AnalyticsException.class,
                        exception -> assertThat(exception.reason()).isEqualTo(AnalyticsException.Reason.NOT_FOUND));
        verify(departmentCostRepository, never()).create(any());
    }

    @Test
    void shouldReturnNewestCostsFirst() {
        final DepartmentCost older = new DepartmentCost(new DepartmentCostId(1L), KATOWICE_CODE,
                DepartmentCostCategory.FUEL, BigDecimal.TEN, Currency.PLN, LocalDate.of(2026, 11, 2), null, MANAGER,
                NOW);
        final DepartmentCost newer = cost(KATOWICE_CODE, DepartmentCostCategory.CLAIM, "5.00", Currency.PLN);
        when(departmentCostRepository.find(NOVEMBER, Optional.empty())).thenReturn(List.of(older, newer));

        assertThat(port.find(new FindDepartmentCostsCommand(NOVEMBER, Optional.empty())))
                .containsExactly(newer, older);
    }

    @Test
    void shouldDeleteExistingCost() {
        final DepartmentCost existing = cost(KATOWICE_CODE, DepartmentCostCategory.FUEL, "5.00", Currency.PLN);
        when(departmentCostRepository.findById(existing.costId())).thenReturn(Optional.of(existing));

        port.delete(existing.costId());

        verify(departmentCostRepository).delete(existing.costId());
    }

    @Test
    void shouldRejectDeletingMissingCost() {
        final DepartmentCostId costId = new DepartmentCostId(404L);
        when(departmentCostRepository.findById(costId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> port.delete(costId))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("404");
        verify(departmentCostRepository, never()).delete(any());
    }

    private static RecordDepartmentCostCommand command(final DepartmentCode departmentCode) {
        return new RecordDepartmentCostCommand(departmentCode, DepartmentCostCategory.FUEL,
                new BigDecimal("320.4"), Currency.PLN, LocalDate.of(2026, 11, 16), "Fuel card");
    }
}
