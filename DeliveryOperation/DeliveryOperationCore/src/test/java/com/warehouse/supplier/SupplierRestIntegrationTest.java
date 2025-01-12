package com.warehouse.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.supplier.configuration.SupplierTestConfiguration;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.vo.SupplierAddResponse;
import com.warehouse.supplier.domain.port.primary.SupplyPortImpl;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = SupplierTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/supplier.xml")
public class SupplierRestIntegrationTest {


    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplyPortImpl supplyPort;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierCodeGeneratorService supplierCodeGeneratorService;

    private final String depotCode = "KT1";
    private final String firstName = "Sebastian";
    private final String lastName = "Soja";
    private final String telephone = "123";
    private final String supplierCode = "supplierCode";


    @Test
    void shouldCreateSuppliers() {
        // given
        final SupplierAddRequest request = buildSupplierAddRequest();
        // when
		final List<SupplierAddResponse> supplierAddResponseList = supplyPort
				.createMultipleSuppliers(List.of(request));
        // then
        assertThat(supplierAddResponseList.get(0).getSupplier()).isNotNull();
    }

    @Test
    void shouldCreateSuppliersWithGivenSupplierCode() {
        // given
        final SupplierAddRequest request = SupplierAddRequest.builder()
                .depotCode(depotCode)
                .firstName(firstName)
                .lastName(lastName)
                .telephone(telephone)
                .supplierCode(supplierCode)
                .build();
        // when
        final List<SupplierAddResponse> supplierAddResponseList = supplyPort
                .createMultipleSuppliers(List.of(request));
        // then
        assertThat(supplierAddResponseList.get(0).getSupplier()).isNotNull();
        assertEquals(supplierAddResponseList.get(0).getSupplier().getSupplierCode(), supplierCode);
    }

    @Test
    void shouldFindAllSuppliers() {
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertFalse(suppliers.isEmpty());
    }

    @Test
    void shouldUpdateSupplier() {
        // given
        final Supplier supplier = Supplier.builder()
                .supplierCode("abcdef")
                .firstName("Anna")
                .lastName("test")
                .telephone("123")
                .departmentCode("TST")
                .active(Boolean.TRUE)
                .build();
        // when
        final Supplier updatedSupplier = supplyPort.updateSupplier(supplier);
        // then
        assertEquals("Anna", updatedSupplier.getFirstName());
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertFalse(suppliers.isEmpty());
    }

    @Test
    void shouldFindSupplierByCode() {
        // given
        final String code = "code";
        // when
        final Supplier supplier = supplyPort.findSupplierByCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindSupplierByCode() {
        // given
        final String code = "fakeCode";
        // when
        final Executable executable = () -> supplyPort.findSupplierByCode(code);
        // then
        final SupplierNotFoundException supplierNotFoundException =
                assertThrows(SupplierNotFoundException.class, executable);
        assertEquals(expectedToBe("Supplier was not found"), supplierNotFoundException.getMessage());
    }

    @Test
    void shouldFindManyByDepotCode() {
        // given
        final String code = "code";
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByDepotCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindManyByDepotCode() {
        // given
        final String code = "code";
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

    private <T> T expectedToBe(T t) {
        return t;
    }
}
