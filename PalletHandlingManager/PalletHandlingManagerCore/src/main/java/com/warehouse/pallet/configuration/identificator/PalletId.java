package com.warehouse.pallet.configuration.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public record PalletId(String value) {

}
