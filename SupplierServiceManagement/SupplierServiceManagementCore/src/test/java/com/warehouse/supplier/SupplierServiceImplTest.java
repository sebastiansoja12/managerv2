package com.warehouse.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import com.warehouse.supplier.domain.model.SupplierCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.service.SupplierServiceImpl;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {


    @Mock
    private SupplierRepository supplierRepository;

    private SupplierServiceImpl service;

    @BeforeEach
    void setup() {
        service = new SupplierServiceImpl(supplierRepository);
    }

    @Test
    void shouldCreateSuppliers() {
        // given
        final Supplier supplier = createSupplier();

        final SupplierModelRequest request = mock(SupplierModelRequest.class);

        when(supplierRepository.createMultipleSuppliers(Collections.singletonList(supplier))).thenReturn(
                Collections.singletonList(request));
        // when
        final List<Supplier> supplierAddResponseList = service
                .createMultipleSuppliers(List.of(supplier));
        // then
        assertThat(supplierAddResponseList.get(0)).isNotNull();
    }

    @Test
    void shouldFindAllSuppliers() {
        // given
        final Supplier supplier = createSupplier();
        when(supplierRepository.findAll()).thenReturn(
                Collections.singletonList(supplier));
        // when
        final List<Supplier> suppliers = service.findAll();
        // then
        assertFalse(suppliers.isEmpty());
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        when(supplierRepository.findAll()).thenReturn(
                Collections.emptyList());
        // when
        final List<Supplier> suppliers = service.findAll();
        // then
        assertTrue(suppliers.isEmpty());
    }

    @Test
    void shouldFindSupplierByCode() {
        // given
        final String code = "code";
        when(supplierRepository.findByCode(code)).thenReturn(createSupplier());
        // when
        final Supplier supplier = service.findSupplierByCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindSupplierByCode() {
        // given
        final String code = "code";
        final SupplierNotFoundException exception = new SupplierNotFoundException("Supplier was not found");
        doThrow(exception)
                .when(supplierRepository)
                .findByCode(code);
        // when
        final Executable executable = () -> service.findSupplierByCode(code);
        // then
        final SupplierNotFoundException supplierNotFoundException =
                assertThrows(SupplierNotFoundException.class, executable);
        assertEquals(expectedToBe("Supplier was not found"), supplierNotFoundException.getMessage());
    }

    @Test
    void shouldFindManyByDepotCode() {
        // given
        final String code = "code";
        when(supplierRepository.findByDepotCode(code)).thenReturn(List.of(createSupplier()));
        // when
        final List<Supplier> supplier = service.findSuppliersByDepotCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindManyByDepotCode() {
        // given
        final String code = "code";
        when(supplierRepository.findByDepotCode(code)).thenReturn(Collections.emptyList());
        // when
        final List<Supplier> supplier = service.findSuppliersByDepotCode(code);
        // then
        assertTrue(supplier.isEmpty());
    }

    private static SupplierCreateRequest buildSupplierAddRequest() {
        return SupplierCreateRequest.builder()
                .depotCode("KT1")
                .firstName("Sebastian")
                .lastName("Soja")
                .telephone("123")
                .build();
    }

    private Supplier createSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setFirstName("Sebastian");
        supplier.setLastName("Soja");
        supplier.setSupplierCode("code");
        supplier.setTelephone("123");
        supplier.setDepartmentCode("KT1");
        return supplier;
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
