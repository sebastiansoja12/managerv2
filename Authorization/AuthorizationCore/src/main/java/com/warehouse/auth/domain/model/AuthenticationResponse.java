package com.warehouse.auth.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {

    String authenticationToken;

    String refreshToken;

}
