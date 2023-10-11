package com.warehouse.supplier;

import com.warehouse.supplier.configuration.SupplierTestConfiguration;
import com.warehouse.supplier.domain.model.Depot;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.SupplierService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = SupplierTestConfiguration.class)
@Disabled
public class DeliveryServiceHsqlDbTest {

    @Autowired
    private SupplierService service;

    @BeforeEach
    void setUp() {
        service.create(createSupplier());
    }

    @Test
    void shouldReturnAllSuppliers() {
        // given && when
        final List<Supplier> suppliers = service.findAll();
        // then
        assertThat(suppliers.size()).isGreaterThan(0);
    }

    @Test
    void shouldReturnBySupplierCode() {
        // given
        final String supplierCode = "test";
        // when
        final Supplier supplierByCode = service.findSupplierByCode(supplierCode);
        // then
        assertThat(supplierByCode).isNotNull();
    }

    @Test
    void shouldCreateMultipleSuppliers() {
        // given
        final Supplier supplier1 = new Supplier();
        supplier1.setFirstName("test");
        supplier1.setLastName("test");
        supplier1.setSupplierCode("test1");
        supplier1.setDepotCode("TST");
        supplier1.setTelephone("123");

        final Supplier supplier2 = new Supplier();
        supplier2.setFirstName("test");
        supplier2.setLastName("test");
        supplier2.setDepotCode("TST");
        supplier2.setSupplierCode("test2");
        supplier2.setTelephone("123");

        final List<Supplier> suppliers = Arrays.asList(supplier1, supplier2);

        // when
        service.createMultipleSuppliers(suppliers);

        // then
        final List<Supplier> supplierList = service.findAll();
        assertTrue(supplierList.size() == 3);
    }

    @Test
    void shouldNotCreateSupplierWhenOneAlreadyExists() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setFirstName("test");
        supplier.setLastName("test");
        supplier.setSupplierCode("test");
        supplier.setTelephone("123");
        // when
        final Executable executable = () -> service.create(supplier);

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, executable);
    }

    @Test
    void shouldNotCreateSuppliersWhenOneAlreadyExists() {
        // given
        final Supplier supplier = new Supplier();
        supplier.setFirstName("test");
        supplier.setLastName("test");
        supplier.setSupplierCode("test1");
        supplier.setTelephone("123");

        final Supplier supplier2 = new Supplier();
        supplier2.setFirstName("test");
        supplier2.setLastName("test2");
        supplier2.setSupplierCode("test");
        supplier2.setTelephone("123");


        final Supplier supplier3 = new Supplier();
        supplier3.setFirstName("test");
        supplier3.setLastName("test");
        supplier3.setSupplierCode("test");
        supplier3.setTelephone("123");

        final List<Supplier> suppliers = Arrays.asList(supplier, supplier2, supplier3);

        // when
        final Executable executable = () -> service.createMultipleSuppliers(suppliers);

        // then
        Assertions.assertThrows(DataIntegrityViolationException.class, executable);
    }

    private Supplier createSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setFirstName("test");
        supplier.setLastName("test");
        supplier.setDepotCode("TST");
        supplier.setSupplierCode("test");
        supplier.setTelephone("123");
        return supplier;
    }

    private Depot createDepot(Long id, String depotCode) {
        final Depot depot = new Depot();
        depot.setId(id);
        depot.setDepotCode(depotCode);
        depot.setCity("Test");
        depot.setStreet("test");
        depot.setCountry("Poland");
        return depot;
    }


}
