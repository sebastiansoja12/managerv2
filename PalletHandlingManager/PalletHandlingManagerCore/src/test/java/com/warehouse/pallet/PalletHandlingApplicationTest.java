package com.warehouse.pallet;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
public class PalletHandlingApplicationTest {

    @Test
    void contextLoads() {

    }

    @Test
    void shouldLoadContext() {
        ApplicationModules.of(PalletHandlingApplication.class).verify();
    }
}
