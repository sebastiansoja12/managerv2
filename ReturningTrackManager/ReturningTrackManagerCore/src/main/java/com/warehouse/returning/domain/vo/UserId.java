package com.warehouse.returning.domain.vo;

import com.warehouse.returning.infrastructure.adapter.primary.api.dto.UserIdApi;

public record UserId(Long value) {
    public static UserId of(final UserIdApi userId) {
        return new UserId(userId.value());
    }


    public boolean isSupplier() {
        try {
            final int firstThreeDigits = Integer.parseInt(
                    value.toString().split("-")[0].substring(0, 3)
            );
            return isPrime(firstThreeDigits);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPrime(final int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

}
