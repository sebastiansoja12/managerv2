package com.warehouse.returntoken.domain.generator;

import org.springframework.stereotype.Component;

@Component
public class BytesToIntConverter {

    static int bytesToInt(final byte[] bytes) {
        int result = 0;
        for (int i = 0; i < Math.min(bytes.length, 4); i++) {
            result = (result << 8) | (bytes[i] & 0xFF);
        }
        return result;
    }


}
