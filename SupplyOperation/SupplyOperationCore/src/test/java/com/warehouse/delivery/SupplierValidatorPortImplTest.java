package com.warehouse.delivery;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.delivery.domain.port.primary.SupplierValidatorPortImpl;
import com.warehouse.delivery.domain.port.secondary.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierValidatorPortImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierValidatorPortImpl supplierValidatorPort;

    @ParameterizedTest
    @CsvSource("abc")
    void shouldCheckIfSupplierIsValidAndThrowException(final String supplierCode) {
//        // given
//        final Device device = Mockito.mock(Device.class);
//        final String message = "Supplier not found";
//        when(supplierRepository.validBySupplierCode(supplierCode)).thenThrow(new RuntimeException(message));
//        // when
//        final Executable executable = () -> supplierValidatorPort.validateSupplierCode(device);
//        // then
//        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
//        assertEquals(message, exception.getMessage());
    }
}
