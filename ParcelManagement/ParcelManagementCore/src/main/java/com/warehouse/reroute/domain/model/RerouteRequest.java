package com.warehouse.reroute.domain.model;


import static com.warehouse.reroute.domain.exception.enumeration.RerouteExceptionCodes.REROUTE_101;

import com.warehouse.reroute.domain.exception.EmailNotFoundException;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RerouteRequest {

    @NonNull
    private Long parcelId;

    private String email;

    public String getEmail() {
        if (email == null) {
            throw new EmailNotFoundException(REROUTE_101);
        }
        return email;
    }
}
