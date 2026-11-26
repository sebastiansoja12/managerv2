package com.warehouse.eventoutbox.integration.registry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.integration.registry.scanned.ScannedTestEvents;

class IntegrationEventTypeScannerTest {

    private static final String SCANNED_PACKAGE = "com.warehouse.eventoutbox.integration.registry.scanned";

    @Test
    void shouldRegisterEveryClassDeclaringIntegrationEventType() {
        final IntegrationEventTypeScanner scanner = new IntegrationEventTypeScanner(getClass().getClassLoader());

        final IntegrationEventRegistry registry = scanner.scan(SCANNED_PACKAGE);

        assertThat(registry.definitions())
                .extracting(definition -> definition.eventClass().getName())
                .containsExactlyInAnyOrder(ScannedTestEvents.PalletLoaded.class.getName(),
                        ScannedTestEvents.PalletUnloaded.class.getName());
        assertThat(registry.definitionOf(ScannedTestEvents.PalletLoaded.class).topic()).isEqualTo("pallet.loaded");
        assertThat(registry.definitionOf(ScannedTestEvents.PalletLoaded.class)
                .messageKey(new ScannedTestEvents.PalletLoaded("P-1"))).contains("P-1");
    }
}
