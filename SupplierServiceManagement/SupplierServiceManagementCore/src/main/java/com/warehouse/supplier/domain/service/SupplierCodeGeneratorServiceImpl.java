package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.identificator.SupplierCode;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;


public class SupplierCodeGeneratorServiceImpl implements SupplierCodeGeneratorService {

    private final static Integer SUPPLIER_CODE_LENGTH = 6;

    
    public String generate() {
        final StringBuilder builder = new StringBuilder();
        final Random r = new Random();
        for (int i = 0; i < SUPPLIER_CODE_LENGTH; i++) {
            final char c = (char)(r.nextInt(26) + 'a');
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public SupplierCode generate(final SupplierCode supplierCode) {
        final SupplierCode generatedSupplierCode;
		if (supplierCode != null && StringUtils.isNotBlank(supplierCode.value())
				&& supplierCode.value().length() == SUPPLIER_CODE_LENGTH) {
            generatedSupplierCode = supplierCode;
        } else {
            generatedSupplierCode = new SupplierCode(generate());
        }
        return generatedSupplierCode;
    }
}
