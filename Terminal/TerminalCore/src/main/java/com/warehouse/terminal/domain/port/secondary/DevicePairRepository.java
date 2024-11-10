package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.terminal.domain.model.Terminal;
import com.warehouse.terminal.domain.vo.DevicePairId;

public interface DevicePairRepository {
    void pair(final Terminal terminal, final DevicePairId devicePairId);
}
