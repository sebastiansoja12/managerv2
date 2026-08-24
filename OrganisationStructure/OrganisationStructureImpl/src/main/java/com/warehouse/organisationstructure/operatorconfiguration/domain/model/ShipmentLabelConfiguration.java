package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentLabelConfiguration {
    private boolean autoGenerateLabels;
    private boolean includeReturnLabel;
    private boolean attachPackingSlip;
    private LabelFormat labelFormat;

    public ShipmentLabelConfiguration() {
    }

    public ShipmentLabelConfiguration(final boolean autoGenerateLabels,
                                      final boolean includeReturnLabel,
                                      final boolean attachPackingSlip,
                                      final LabelFormat labelFormat) {
        this.autoGenerateLabels = autoGenerateLabels;
        this.includeReturnLabel = includeReturnLabel;
        this.attachPackingSlip = attachPackingSlip;
        this.labelFormat = labelFormat;
    }

    public static ShipmentLabelConfiguration defaultConfiguration() {
        return new ShipmentLabelConfiguration(false, false, false, LabelFormat.PDF_A6);
    }

    public boolean isAutoGenerateLabels() { return autoGenerateLabels; }
    public boolean isIncludeReturnLabel() { return includeReturnLabel; }
    public boolean isAttachPackingSlip() { return attachPackingSlip; }
    public LabelFormat getLabelFormat() { return labelFormat; }
}
