package com.warehouse.supplier.domain.model;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.domain.enumeration.SupplierStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SupplierAnonymizationTest {

    @Test
    void shouldRemovePersonalDataAndMarkCourierDeleted() {
        final Supplier supplier = new Supplier(new SupplierId(9L), new SupplierCode("abcdef"), "Adam", "Nowak",
                "+48111222333");
        supplier.changeApiKey("api-key");

        supplier.anonymize();

        assertThat(supplier.getFirstName()).isEqualTo("Deleted");
        assertThat(supplier.getLastName()).isEqualTo("Courier");
        assertThat(supplier.getTelephoneNumber()).isEqualTo("erased");
        assertThat(supplier.getApiKey()).isNull();
        assertThat(supplier.getDriverLicense()).isNull();
        assertThat(supplier.getStatus()).isEqualTo(SupplierStatus.DELETED);
        assertThat(supplier.supplierCode()).isEqualTo(new SupplierCode("abcdef"));
    }
}
