package com.warehouse.process;

import com.warehouse.process.infrastructure.dto.InitializeProcessRequestDto;
import com.warehouse.process.infrastructure.dto.ProcessIdDto;

public interface ProcessHubApiService {
    ProcessIdDto initialize(final InitializeProcessRequestDto request);
}
