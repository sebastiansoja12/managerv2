package com.warehouse.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.model.SupplierAddRequest;
import com.warehouse.supplier.domain.model.SupplierAddResponse;
import com.warehouse.supplier.domain.model.SupplierModelRequest;
import com.warehouse.supplier.domain.port.primary.SupplyPortImpl;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorServiceImpl;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.service.SupplierServiceImpl;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;

@ExtendWith(MockitoExtension.class)
public class SupplierPortImplTest {


    @Mock
    private SupplierRepository supplierRepository;

    private SupplyPortImpl supplyPort;

    private final String depotCode = "KT1";
    private final String firstName = "Sebastian";
    private final String lastName = "Soja";
    private final String telephone = "123";
    private final String supplierCode = "supplierCode";


    @BeforeEach
    void setup() {
        final SupplierService service = new SupplierServiceImpl(supplierRepository);
        final SupplierCodeGeneratorService generatorService = new SupplierCodeGeneratorServiceImpl();
        supplyPort = new SupplyPortImpl(service, generatorService);
    }

    @Test
    void shouldCreateSuppliers() {
        // given
        final SupplierAddRequest request = buildSupplierAddRequest();

        final Supplier supplier = new Supplier();
        supplier.setFirstName("Sebastian");
        supplier.setLastName("Soja");
        supplier.setSupplierCode("code");
        supplier.setTelephone("123");
        supplier.setDepotCode("KT1");

        final List<SupplierModelRequest> supplierModelRequests = Collections
                .singletonList(SupplierModelRequest.builder()
                        .depotCode(depotCode)
                        .firstName(firstName)
                        .lastName(lastName)
                        .telephone(telephone)
                        .build());

        doReturn(supplierModelRequests)
                .when(supplierRepository)
                .createMultipleSuppliers(List.of(supplier));
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

        final Supplier supplier = new Supplier(
                "supplierCode", "Sebastian", "Soja", "123", "KT1"
        );

		final List<SupplierModelRequest> supplierModelRequests = Collections
				.singletonList(SupplierModelRequest.builder()
                        .depotCode(depotCode)
                        .supplierCode(supplierCode)
						.firstName(firstName)
                        .lastName(lastName)
                        .telephone(telephone)
                        .build());
        
        doReturn(supplierModelRequests)
                .when(supplierRepository)
                .createMultipleSuppliers(List.of(supplier));

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
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertFalse(suppliers.isEmpty());
    }

    @Test
    void shouldReturnEmptyList() {
        // given
        // when
        final List<Supplier> suppliers = supplyPort.findAllSuppliers();
        // then
        assertTrue(suppliers.isEmpty());
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
        final String code = "code";
        final SupplierNotFoundException exception = new SupplierNotFoundException("Supplier was not found");

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
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByCode(code);
        // then
        assertThat(supplier).isNotNull();
    }

    @Test
    void shouldNotFindManyBySupplierCode() {
        // given
        final String code = "code";
        // when
        final List<Supplier> supplier = supplyPort.findSuppliersByCode(code);
        // then
        assertTrue(supplier.isEmpty());
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
