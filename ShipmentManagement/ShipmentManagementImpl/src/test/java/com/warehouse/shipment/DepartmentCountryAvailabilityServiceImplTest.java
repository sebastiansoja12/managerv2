package com.warehouse.shipment;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import com.warehouse.shipment.application.service.DepartmentCountryAvailabilityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentCountryAvailabilityServiceImplTest {

    @Mock
    private DepartmentServicePort departmentServicePort;

    @Test
    void shouldReturnTrueWhenDepartmentExistsForCountry() {
        final DepartmentCountryAvailabilityServiceImpl service =
                new DepartmentCountryAvailabilityServiceImpl(departmentServicePort);
        when(departmentServicePort.existsByCountryCode(CountryCode.PL)).thenReturn(true);

        final boolean available = service.isCountryAvailable(CountryCode.PL);

        assertTrue(available);
        verify(departmentServicePort).existsByCountryCode(CountryCode.PL);
    }

    @Test
    void shouldReturnFalseWhenDepartmentDoesNotExistForCountry() {
        final DepartmentCountryAvailabilityServiceImpl service =
                new DepartmentCountryAvailabilityServiceImpl(departmentServicePort);
        when(departmentServicePort.existsByCountryCode(CountryCode.DE)).thenReturn(false);

        final boolean available = service.isCountryAvailable(CountryCode.DE);

        assertFalse(available);
        verify(departmentServicePort).existsByCountryCode(CountryCode.DE);
    }
}
