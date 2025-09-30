package com.warehouse.dangerousgood.domain.port.primary;

import java.util.List;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.enumeration.ClassificationCode;
import com.warehouse.dangerousgood.domain.enumeration.Packaging;
import com.warehouse.dangerousgood.domain.enumeration.StorageRequirement;
import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.model.DangerousGoodChangeWeightRequest;
import com.warehouse.dangerousgood.domain.model.DangerousGoodCreateRequest;
import com.warehouse.dangerousgood.domain.service.DangerousGoodService;
import com.warehouse.dangerousgood.domain.service.ShipmentService;
import com.warehouse.dangerousgood.domain.validator.ClassificationCodeValidator;
import com.warehouse.dangerousgood.domain.validator.NameRequirementValidator;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;
import com.warehouse.dangerousgood.domain.vo.Shipment;

public class DangerousGoodPortImpl implements DangerousGoodPort {

    private final DangerousGoodService dangerousGoodService;

    private final ShipmentService shipmentService;

    public DangerousGoodPortImpl(final DangerousGoodService dangerousGoodService,
                                 final ShipmentService shipmentService) {
        this.dangerousGoodService = dangerousGoodService;
        this.shipmentService = shipmentService;
    }

    @Override
    public void createDangerousGood(final DangerousGoodCreateRequest request) {
        NameRequirementValidator.validateName(request.getName());
        ClassificationCodeValidator.validateClassificationCode(request.getClassificationCode());

        final DangerousGoodId dangerousGoodId = this.dangerousGoodService.nextDangerousGoodId();
        final Shipment shipment = this.shipmentService.findById(request.getShipmentId());

        if (shipment == null) {
            throw new IllegalArgumentException("Shipment not found");
        }

        final ShipmentId shipmentId = request.getShipmentId();
        final String name = request.getName();
        final String description = request.getDescription();
        final ClassificationCode classificationCode = ClassificationCode.valueOf(request.getClassificationCode());
        final List<String> hazardSymbols = cleanHazardSymbols(request.getHazardSymbols());
        final StorageRequirement storageRequirements = StorageRequirement.valueOf(request.getStorageRequirements());
        final String handlingInstructions = request.getHandlingInstructions();
        final Weight weight = request.getWeight();
        final Packaging packaging = Packaging.valueOf(request.getPackaging());
        final boolean flammable = request.isFlammable();
        final boolean corrosive = request.isCorrosive();
        final boolean toxic = request.isToxic();
        final String emergencyContact = request.getEmergencyContact();
        final Country originCountry = request.getCountryOfOrigin();
        final String safetyDataSheet = request.getSafetyDataSheet();

		final DangerousGood dangerousGood = new DangerousGood(dangerousGoodId, shipmentId, name, description,
				classificationCode, hazardSymbols, storageRequirements, handlingInstructions, weight, packaging,
				flammable, corrosive, toxic, emergencyContact, originCountry, safetyDataSheet);

        this.dangerousGoodService.createDangerousGood(dangerousGood);

    }

    @Override
    public void updateDangerousGoodWeight(final DangerousGoodChangeWeightRequest request) {
        final Shipment shipment = this.shipmentService.findById(request.getShipmentId());
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment not found");
        }
        this.dangerousGoodService.updateWeight(request.getDangerousGoodId(), request.getWeight());
    }

    private List<String> cleanHazardSymbols(final List<String> hazardSymbols) {
        return hazardSymbols;
    }
}
