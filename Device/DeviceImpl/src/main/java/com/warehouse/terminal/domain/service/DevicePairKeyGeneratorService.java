package com.warehouse.terminal.domain.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DevicePairKeyGeneratorService {

    public static String generatePairKey() {
        return UUID.randomUUID().toString();
    }
}
