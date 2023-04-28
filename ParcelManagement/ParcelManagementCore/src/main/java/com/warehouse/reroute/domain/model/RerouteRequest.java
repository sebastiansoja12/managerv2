package com.warehouse.reroute.domain.model;


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
            throw new EmailNotFoundException("E-Mail cannot be null");
        }
        return email;
    }
}
