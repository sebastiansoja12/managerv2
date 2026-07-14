package com.warehouse.supplier.domain.service;

import com.warehouse.commonassets.identificator.SupplierCode;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;


public class SupplierCodeGeneratorServiceImpl implements SupplierCodeGeneratorService {

    private final static Integer SUPPLIER_CODE_LENGTH = 6;

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate() {
        StringBuilder builder = new StringBuilder(SUPPLIER_CODE_LENGTH);
        for (int i = 0; i < SUPPLIER_CODE_LENGTH; i++) {
            char c = (char) (RANDOM.nextInt(26) + 'a');
            builder.append(c);
        }
        return builder.toString();
    }

    @Override
    public SupplierCode generate(final SupplierCode supplierCode) {
        final SupplierCode generatedSupplierCode;
		if (supplierCode != null && StringUtils.isNotBlank(supplierCode.value())) {
            generatedSupplierCode = supplierCode;
        } else {
            generatedSupplierCode = new SupplierCode(generate());
        }
        return generatedSupplierCode;
    }
}
