package com.warehouse.auth.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RefreshToken {
    String token;
    Instant createdDate;
}
