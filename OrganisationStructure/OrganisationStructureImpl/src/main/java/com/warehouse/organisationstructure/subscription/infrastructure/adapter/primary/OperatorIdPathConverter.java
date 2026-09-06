package com.warehouse.organisationstructure.subscription.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class OperatorIdPathConverter implements Converter<String, OperatorId> {

    @Override
    public OperatorId convert(@NonNull final String source) {
        final long operatorId = Long.parseLong(source.trim());
        if (operatorId <= 0) {
            throw new IllegalArgumentException("Operator id must be positive");
        }
        return OperatorId.of(operatorId);
    }
}
