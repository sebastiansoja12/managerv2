package com.warehouse.supplier.domain.service;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class SupplierCodeGeneratorServiceImpl implements SupplierCodeGeneratorService {

    private final static Integer SUPPLIER_CODE_LENGTH = 6;

    @Override
    public String generate() {
        final StringBuilder builder = new StringBuilder();
        final Random r = new Random();
        for (int i = 0; i < SUPPLIER_CODE_LENGTH; i++) {
            final char c = (char)(r.nextInt(26) + 'a');
            builder.append(c);
        }
        return builder.toString();
    }
}
