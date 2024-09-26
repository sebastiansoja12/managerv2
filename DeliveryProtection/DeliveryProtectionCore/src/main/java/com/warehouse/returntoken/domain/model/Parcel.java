package com.warehouse.returntoken.domain.model;

import com.warehouse.commonassets.enumeration.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {
    private Long id;
    private Long parcelRelatedId;
    private ParcelStatus parcelStatus;
    private Boolean locked;

    public boolean isLocked() {
        return locked;
    }
}
