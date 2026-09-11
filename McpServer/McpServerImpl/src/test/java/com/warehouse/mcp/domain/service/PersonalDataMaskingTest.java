package com.warehouse.mcp.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.mcp.McpFixture;
import com.warehouse.mcp.domain.vo.MaskedShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentParty;

class PersonalDataMaskingTest {

    private final PersonalDataMasking masking = new PersonalDataMasking();

    @Test
    void shouldKeepOnlyInitialsCityAndPostalCode() {
        final MaskedShipmentParty party = masking.mask(McpFixture.recipient());

        assertThat(party.initials()).isEqualTo("J. N.");
        assertThat(party.email()).isEqualTo("j***@example.com");
        assertThat(party.telephoneNumber()).isEqualTo("***789");
        assertThat(party.city()).isEqualTo("Katowice");
        assertThat(party.postalCode()).isEqualTo("40-001");
        assertThat(party.toString()).doesNotContain("Nowak", "jan.nowak", "600 123");
    }

    @Test
    void shouldHandleMissingAndShortValues() {
        final MaskedShipmentParty party = masking.mask(new ShipmentParty(null, " ", "invalid", "12", null, null, null));

        assertThat(party.initials()).isNull();
        assertThat(party.email()).isEqualTo("***");
        assertThat(party.telephoneNumber()).isEqualTo("***");
    }

    @Test
    void shouldReturnNullForMissingParty() {
        assertThat(masking.mask(null)).isNull();
    }
}
