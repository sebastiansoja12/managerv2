package com.warehouse.organisationstructure.api;

import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;

import java.util.Optional;

public interface OperatorConfigurationApiService {

    Optional<OperatorConfigurationDto> getCurrent();
}
