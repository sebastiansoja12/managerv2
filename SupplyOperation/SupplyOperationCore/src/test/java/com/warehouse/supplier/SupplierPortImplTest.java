package com.warehouse.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.model.SupplierAddResponse;
import com.warehouse.supplier.domain.port.primary.SupplyPortImpl;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;

@ExtendWith(MockitoExtension.class)
public class SupplierPortImplTest {

    @Mock
    private SupplierService service;

    private SupplyPortImpl supplyPort;


    @Mock
    private SupplierCodeGeneratorService generatorService;


    @BeforeEach
    void setup() {
        supplyPort = new SupplyPortImpl(service, generatorService);
    }

    @Test
    void shouldCreateSuppliers() {
        // given
        final SupplierAddRequest request = buildSupplierAddRequest();

        final Supplier supplier = createSupplier();

        when(generatorService.generate()).thenReturn("code");
        when(service.createMultipleSuppliers(Collections.singletonList(supplier))).thenReturn(
                Collections.singletonList(supplier));
        // when
		final List<SupplierAddResponse> supplierAddResponseList = supplyPort
				.createMultipleSuppliers(Stream.of(request).toList());
        // then
        assertThat(supplierAddResponseList.get(0).getSupplier()).isNotNull();
    }

    @Test
    void shouldFindAllSuppliers() {
        // given
        final Supplier supplier = createSupplier();
        when(service.findAll()).thenReturn(
                Collections.singletonList(supplier));
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertFalse(suppliers.isEmpty());
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        when(service.findAll()).thenReturn(
                Collections.emptyList());
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertTrue(suppliers.isEmpty());
    }

    @Test
    void shouldFindSupplierByCode() {
        // given
        final String code = "code";
        when(service.findSupplierByCode(code)).thenReturn(createSupplier());
        // when
        final Supplier supplier = supplyPort.findSupplierByCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindSupplierByCode() {
        // given
        final String code = "code";
        final SupplierNotFoundException exception = new SupplierNotFoundException("Supplier was not found");
        doThrow(exception)
                .when(service)
                .findSupplierByCode(code);
        // when
        final Executable executable = () -> supplyPort.findSupplierByCode(code);
        // then
        final SupplierNotFoundException supplierNotFoundException =
                assertThrows(SupplierNotFoundException.class, executable);
        assertEquals(expectedToBe("Supplier was not found"), supplierNotFoundException.getMessage());
    }

    @Test
    void shouldFindManyBySupplierCode() {
        // given
        final String code = "code";
        when(service.findSuppliersBySupplierCode(code)).thenReturn(List.of(createSupplier()));
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindManyBySupplierCode() {
        // given
        final String code = "code";
        when(service.findSuppliersBySupplierCode(code)).thenReturn(Collections.emptyList());
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByCode(code);
        // then
        assertTrue(supplier.isEmpty());
    }

    @Test
    void shouldFindManyByDepotCode() {
        // given
        final String code = "code";
        when(service.findSuppliersByDepotCode(code)).thenReturn(List.of(createSupplier()));
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByDepotCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindManyByDepotCode() {
        // given
        final String code = "code";
        when(service.findSuppliersByDepotCode(code)).thenReturn(Collections.emptyList());
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByDepotCode(code);
        // then
        assertTrue(supplier.isEmpty());
    }

    private static SupplierAddRequest buildSupplierAddRequest() {
        return SupplierAddRequest.builder()
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
        supplier.setDepotCode("KT1");
        return supplier;
    }

    private <T> T expectedToBe(T t) {
        return t;
    }
}
