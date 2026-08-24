package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LabelFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class ShipmentLabelConfigurationEmbeddable {

    @Column(name = "shipment_auto_generate_labels")
    private boolean autoGenerateLabels;

    @Column(name = "shipment_include_return_label")
    private boolean includeReturnLabel;

    @Column(name = "shipment_attach_packing_slip")
    private boolean attachPackingSlip;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_label_format")
    private LabelFormat labelFormat;

    public ShipmentLabelConfigurationEmbeddable() {
    }

    public static ShipmentLabelConfigurationEmbeddable from(
            final ShipmentLabelConfiguration configuration) {
        final ShipmentLabelConfiguration source = configuration != null
                ? configuration
                : ShipmentLabelConfiguration.defaultConfiguration();
        final ShipmentLabelConfigurationEmbeddable embeddable = new ShipmentLabelConfigurationEmbeddable();
        embeddable.autoGenerateLabels = source.isAutoGenerateLabels();
        embeddable.includeReturnLabel = source.isIncludeReturnLabel();
        embeddable.attachPackingSlip = source.isAttachPackingSlip();
        embeddable.labelFormat = source.getLabelFormat();
        return embeddable;
    }

    public ShipmentLabelConfiguration toModel() {
        return new ShipmentLabelConfiguration(
                autoGenerateLabels,
                includeReturnLabel,
                attachPackingSlip,
                labelFormat
        );
    }
}
