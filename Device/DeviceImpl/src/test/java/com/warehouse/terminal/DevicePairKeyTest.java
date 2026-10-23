package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.warehouse.terminal.domain.vo.DevicePairKey;

class DevicePairKeyTest {

    @Test
    void shouldHashPairKeyWithSha256MatchingDatabaseMigration() {
        assertEquals("ae98625d8bbd5ab0b9a352478d279f9d92d0ce3568ebd1b520138295371386da",
                new DevicePairKey("e2e-terminal-0001-pair-key").hash());
    }

    @Test
    void shouldNotExposeRawPairKeyInHash() {
        final DevicePairKey pairKey = new DevicePairKey("pair-key");

        assertNotEquals(pairKey.value(), pairKey.hash());
    }
}
