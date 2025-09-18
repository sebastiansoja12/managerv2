package com.warehouse.dangerousgood.domain.vo;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.enumeration.ClassificationCode;
import com.warehouse.dangerousgood.domain.enumeration.Packaging;
import com.warehouse.dangerousgood.domain.enumeration.StorageRequirement;

public record GoodSnapshot(DangerousGoodId dangerousGoodId,
                           ShipmentId shipmentId,
                           String name,
                           String description,
                           ClassificationCode classificationCode,
                           String hazardSymbols,
                           StorageRequirement storageRequirement,
                           String handlingInstructions,
                           Weight weight,
                           Packaging packaging,
                           boolean flammable,
                           boolean corrosive,
                           boolean toxic,
                           String emergencyContact,
                           Country countryOfOrigin,
                           String safetyDataSheet
) {}
