package com.warehouse.driverhub.domain.vo;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.driverhub.domain.enumeration.TransportType;

public sealed interface CarrierChange {

    record Name(String value) implements CarrierChange {
    }

    record Tax(TaxId value) implements CarrierChange {
    }

    record Phone(String value) implements CarrierChange {
    }

    record Email(String value) implements CarrierChange {
    }

    record DefaultTransport(TransportType value) implements CarrierChange {
    }

    record Active(boolean value) implements CarrierChange {
    }
}
