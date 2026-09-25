package com.warehouse.qrcode.domain.port.secondary;

import com.warehouse.qrcode.domain.enumeration.LabelFormat;

public interface LabelConfigurationServicePort {
    LabelFormat currentLabelFormat();
}
