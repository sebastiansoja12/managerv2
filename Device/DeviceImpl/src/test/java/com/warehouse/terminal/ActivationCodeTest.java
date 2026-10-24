package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.warehouse.terminal.domain.vo.ActivationCode;

class ActivationCodeTest {

    @Test
    void shouldGenerateGroupedCodeWithoutAmbiguousCharacters() {
        final ActivationCode activationCode = ActivationCode.generate();

        assertTrue(activationCode.value().matches("[A-HJ-NP-Z2-9]{4}-[A-HJ-NP-Z2-9]{4}"));
    }

    @Test
    void shouldHashCodeIgnoringCaseAndSeparators() {
        final ActivationCode typed = new ActivationCode("abcd efgh");
        final ActivationCode scanned = new ActivationCode("ABCD-EFGH");

        assertEquals(scanned.hash(), typed.hash());
    }

    @Test
    void shouldProduceDifferentHashesForDifferentCodes() {
        assertNotEquals(new ActivationCode("ABCD-EFGH").hash(), new ActivationCode("ABCD-EFGJ").hash());
    }
}
