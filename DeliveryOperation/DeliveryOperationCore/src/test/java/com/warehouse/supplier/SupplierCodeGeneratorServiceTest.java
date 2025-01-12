package com.warehouse.supplier;

import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SupplierCodeGeneratorServiceTest {

    private final SupplierCodeGeneratorService codeGeneratorService = new SupplierCodeGeneratorServiceImpl();

    @Test
    void shouldGenerateSixCharactersSupplierCode() {
        // given
        final int length = 6;
        // when
        final String code = codeGeneratorService.generate();
        // then
        assertThat(code.length()).isEqualTo(length);
    }
}
