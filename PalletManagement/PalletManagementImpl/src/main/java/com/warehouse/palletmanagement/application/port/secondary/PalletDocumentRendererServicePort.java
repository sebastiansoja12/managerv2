package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.palletmanagement.domain.model.PalletDocumentData;

public interface PalletDocumentRendererServicePort {
    byte[] renderLabel(final PalletDocumentData data);
    byte[] renderTransportDocument(final PalletDocumentData data);
}
