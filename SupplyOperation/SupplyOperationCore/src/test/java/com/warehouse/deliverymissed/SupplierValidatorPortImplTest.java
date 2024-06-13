package com.warehouse.deliverymissed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.deliverymissed.domain.port.primary.SupplierValidatorPortImpl;
import com.warehouse.deliverymissed.domain.port.secondary.SupplierRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierValidatorPortImplTest {


    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierValidatorPortImpl supplierValidatorPort;

    @ParameterizedTest
    @CsvSource("abc")
    void shouldCheckIfSupplierIsValidAndThrowException(final String supplierCode) {
        // given
        final String message = "Supplier not found";
        when(supplierRepository.validBySupplierCode(supplierCode)).thenThrow(new RuntimeException(message));
        // when
        final Executable executable = () -> supplierValidatorPort.validateSupplierCode(supplierCode);
        // then
        final RuntimeException exception = assertThrows(RuntimeException.class, executable);
        assertEquals(message, exception.getMessage());
    }
}
