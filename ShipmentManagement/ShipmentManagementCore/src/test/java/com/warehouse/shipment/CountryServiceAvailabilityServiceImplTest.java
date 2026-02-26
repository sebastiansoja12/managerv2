package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.domain.port.secondary.DepartmentRepository;
import com.warehouse.shipment.domain.service.CountryServiceAvailabilityServiceImpl;

@ExtendWith(MockitoExtension.class)
class CountryServiceAvailabilityServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    void shouldReturnTrueWhenDepartmentExistsForCountry() {
        final CountryServiceAvailabilityServiceImpl service =
                new CountryServiceAvailabilityServiceImpl(departmentRepository);
        when(departmentRepository.existsAnyByCountryCode(CountryCode.PL)).thenReturn(true);

        final boolean available = service.isCountryAvailable(CountryCode.PL);

        assertTrue(available);
        verify(departmentRepository).existsAnyByCountryCode(CountryCode.PL);
    }

    @Test
    void shouldReturnFalseWhenDepartmentDoesNotExistForCountry() {
        final CountryServiceAvailabilityServiceImpl service =
                new CountryServiceAvailabilityServiceImpl(departmentRepository);
        when(departmentRepository.existsAnyByCountryCode(CountryCode.DE)).thenReturn(false);

        final boolean available = service.isCountryAvailable(CountryCode.DE);

        assertFalse(available);
        verify(departmentRepository).existsAnyByCountryCode(CountryCode.DE);
    }
}
